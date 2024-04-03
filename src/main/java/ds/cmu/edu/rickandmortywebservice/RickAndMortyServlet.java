package ds.cmu.edu.rickandmortywebservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name="RickAndMortyServlet",
        urlPatterns={"/getRickAndMortyInfo"})
public class RickAndMortyServlet extends HttpServlet {
    RickAndMortyModel ram = null;
    List<String> planetResults = null;
    String searchTerm = null;
    Map<String, Object> characterResults = null;

    @Override
    public void init() {
        ram = new RickAndMortyModel();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException {
        // store the user's selected search option: either character or planet
        String option = request.getParameter("option");

        if (option != null) {
            if (option.equals("planet")) {
                planetResults = ram.getPlanetInfo();

                // Convert the results to JSON
                Gson gson = new Gson();
                String json = gson.toJson(planetResults);

                // Write the JSON response
//                response.setContentType("application/json");
//                response.setCharacterEncoding("UTF-8");
//                response.getWriter().write(json);
                PrintWriter pw = response.getWriter();
                pw.print("application/json");
                pw.print("UTF-8");
                pw.print(json);

            } else if (option.equals("character")){
                searchTerm = request.getParameter("searchTerm");

                characterResults = ram.getCharacterInfo(searchTerm);

                // Convert the results to JSON
                Gson gson = new Gson();
                String json = gson.toJson(characterResults);

                // Write the JSON response
                PrintWriter pw = response.getWriter();
                pw.print("application/json");
                pw.print("UTF-8");
                pw.print(json);
            }
        }

        /* logic for when the user sumbits all required input, the user
            could move to the first result page */
//        String nextView = "/index.jsp";

        // transfer control over next view
//        RequestDispatcher view = request.getRequestDispatcher(nextView);
//        view.forward(request, response);
    }
}
