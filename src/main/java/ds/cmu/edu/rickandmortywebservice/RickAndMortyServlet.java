package ds.cmu.edu.rickandmortywebservice;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name="RickAndMortyServlet",
        urlPatterns={"/"})
public class RickAndMortyServlet extends HttpServlet {
    RickAndMortyModel ram = null;
    List<String> planetResults = null;
    String searchTerm = null;
    String option = null;
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
        option = request.getParameter("option");
        request.setAttribute("option", option);

        if (option.equals("planet")){
            planetResults = ram.getPlanetInfo();
            request.setAttribute("planetResults", planetResults);

            // Convert the results to JSON
            Gson gson = new Gson();
            String json = gson.toJson(planetResults);

            // Write the JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        } else if (option.equals("character")){
            searchTerm = request.getParameter("searchTerm");
            request.setAttribute("searchTerm", searchTerm);

            characterResults = ram.getCharacterInfo(searchTerm);
            request.setAttribute("characterResults", characterResults);

            // Convert the results to JSON
            Gson gson = new Gson();
            String json = gson.toJson(characterResults);

            // Write the JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }

        /* logic for when the user sumbits all required input, the user
            could move to the first result page */
        String nextView = "/index.jsp";

        // transfer control over next view
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response);
    }
}
