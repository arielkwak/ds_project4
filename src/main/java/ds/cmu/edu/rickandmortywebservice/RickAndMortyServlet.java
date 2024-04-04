package ds.cmu.edu.rickandmortywebservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.bson.Document;

@WebServlet(name="RickAndMortyServlet",
        urlPatterns={"/getRickAndMortyInfo", "/analysisDashboard"})
public class RickAndMortyServlet extends HttpServlet {
    RickAndMortyModel ram = null;
    LoggingModel lm = null;
    List<String> planetResults = null;
    String searchTerm = null;
    Map<String, Object> characterResults = null;

    @Override
    public void init() {
        ram = new RickAndMortyModel();
        lm = new LoggingModel();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException {
        // store the user's selected search option: either character or planet
        String option = request.getParameter("option");
        String model = request.getHeader("User-Agent"); // This is a simple way to get the phone model, but it might not work for all phones
        Date timestamp = new Date(); // The timestamp when the request was received

        if (option != null) {
            if (option.equals("planet")) {
                planetResults = ram.getPlanetInfo();

                // Convert the results to JSON
                Gson gson = new Gson();
                String json = gson.toJson(planetResults);

                // Write the JSON response
                PrintWriter pw = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                pw.print(json);

                // Log the request and response
                lm.logRequestResponse(option, null, model, requestToString(request), timestamp, json);

            } else if (option.equals("character")){
                searchTerm = request.getParameter("searchTerm");

                characterResults = ram.getCharacterInfo(searchTerm);

                // Convert the results to JSON
                Gson gson = new Gson();
                String json = gson.toJson(characterResults);

                // Write the JSON response
                PrintWriter pw = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                pw.print(json);

                // Log the request and response
                lm.logRequestResponse(option, searchTerm, model, requestToString(request), timestamp, json);
            }
        }

        // *****LOGGING*****
        // set up dashboard for analytics
        List<Document> logs = lm.getLogs();

        // Store the logs in the request scope
        request.setAttribute("logs", logs);

        // *****OPERATIONS*****
        // Calculate the operations analytics
        long planetCount = lm.getOptionCount("planet");
        long characterCount = lm.getOptionCount("character");
        List<Document> topSearchTerms = lm.getTopSearchTerms(5);
        List<Document> modelCounts = lm.getModelCounts();

        // Store the operations analytics in the request scope
        request.setAttribute("planetCount", planetCount);
        request.setAttribute("characterCount", characterCount);
        request.setAttribute("topSearchTerms", topSearchTerms);
        request.setAttribute("modelCounts", modelCounts);

        String nextView = "dashboard.jsp";
        if(request.getRequestURI().contains("/analysisDashboard")){
            // transfer control over next view
            RequestDispatcher view = request.getRequestDispatcher(nextView);
            view.forward(request, response);
        }
    }

    public String requestToString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("URL: ").append(request.getRequestURL()).append("\n");
        sb.append("Method: ").append(request.getMethod()).append("\n");
        sb.append("Parameters: ").append("\n");

        Map<String, String[]> paramMap = request.getParameterMap();
        for (String paramName : paramMap.keySet()) {
            sb.append("  ").append(paramName).append(": ");
            String[] paramValues = paramMap.get(paramName);
            sb.append(Arrays.toString(paramValues)).append("\n");
        }

        return sb.toString();
    }
}
