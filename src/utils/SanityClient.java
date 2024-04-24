package utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.prefs.Preferences;

import org.json.JSONObject;
import org.json.JSONArray;

public class SanityClient {

    public static JSONArray client(String query) throws URISyntaxException, IOException, InterruptedException {
        // This function is responsible of returning a specific data from the sanity
        // database

        // ............

        // Fetch information using HTTP requst from Sanity database
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(
                        "https://5opn2rfv.api.sanity.io/v1/data/query/production?query=" + query))
                .header("Authorization",
                        "Bearer skxLvWmowvAQ1sl8GDxETQw8HPeJ04X6K8xmiWM4x2FaaNS9DpJyRUzw9DDRrbzPX78rLYA9CxmZNnHYyaJMFWvp4y0kXK7TytBn8QECCsSaskrA9k1nw511lYyY4sjaxWqzUubGTKCNZq5zBolwtKGXta8HLVl5IC7nk1efaR65X5iMr3Yy")
                .build();

        // Storing Sanity response in a `response` variable as a string
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // convert the `response` string to a json object to extract the `result` from
        // the sanity response
        JSONObject responseBody = new JSONObject(response.body());
        JSONArray result = responseBody.getJSONArray("result");

        return result;
    }

    public static boolean isUserExists(String email, String password)
            throws URISyntaxException, IOException, InterruptedException {

        // Check if the logged in user's email and password correct, if so then set his
        // data to `UserData` class in order to use them anytime later

        String query = "*[userEmail=='" + email + "'%26%26userPassword=='" + password + "']"; // query to the database

        JSONArray result = client(query); // store the returned array result

        if (result.length() > 0) {
            // in case the result is not empty (there is a student with the same email and
            // password) then set his data to the `UserData` class

            JSONObject ObjResult = (JSONObject) result.get(0);
            UserData.setUserData(ObjResult); // Save user data in a class
            return true;

        } else
            // in case not (email or password are incorrect) then return `false`
            return false;
    }
}
