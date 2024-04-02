package ds.cmu.edu.rickandmortywebservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RickAndMortyModel {
    private List<Map<String, Object>> results = null;
    private String apiURL = null;

    public void fetchData() throws IOException{
        try{
            // connect url
            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // process the url response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            // go through each line of the read api and append it to the content
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            Gson gson = new Gson();

            // Parse the response as a map
            Map<String, Object> data = gson.fromJson(content.toString(), new TypeToken<Map<String, Object>>() {}.getType());

            // Get the results field, which is a list of maps
            results = (List<Map<String, Object>>) data.get("results");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getPlanetInfo(){
        List<String> planets = new ArrayList<>();
        apiURL = "https://rickandmortyapi.com/api/character";
        try{
            fetchData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map<String, Object> result : results) {
            planets.add((String) result.get("name"));
        }
        return planets;
    }

    public Map<String, Object> getCharacterInfo(String searchTerm){
        Map<String, Object> characters = new HashMap<>();
        apiURL = "https://rickandmortyapi.com/api/location";
        try{
            fetchData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Map<String, Object> d: results){
            String name = ((String)d.get("name")).toLowerCase();
            if(name.contains(searchTerm)){
                characters.put("Name", d.get(name));
                characters.put("Status", d.get("status"));
                characters.put("Species", d.get("species"));
                characters.put("Gender", d.get("gender"));

                // Print the origin information
                Map<String, Object> origin = (Map<String, Object>) d.get("origin");
                characters.put("Origin", origin.get("name"));

                // Print the location information
                Map<String, Object> location = (Map<String, Object>) d.get("location");
                characters.put("Location", location.get("name"));
            }
        }
        return characters;
    }
}
