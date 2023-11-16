package seng202.team5.managers;

import com.google.gson.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import seng202.team5.models.data.EmergencyService;
import seng202.team5.models.data.Location;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class to handle requesting location from Nominatim Geolocation API
 */
public class Geolocation {

    Gson gson = new Gson();

    /**
     * Runs a query with the address given and finds the most applicable lat, lng co-ordinates
     * @param address address to find lat, lng for
     */
    public Location queryAddress(String address) {
        address = address.replace(' ', '+');
        address = address.replace("'", "\\'");
        try {
            // Creating the http request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(
                    URI.create("https://nominatim.openstreetmap.org/search?q=" + address + ",+New+Zealand&format=json")
            ).build();
            // Getting the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parsing the json response to get the latitude and longitude co-ordinates
            JSONParser parser = new JSONParser();
            JSONArray results = (JSONArray)  parser.parse(response.body());
            JSONObject bestResult = (JSONObject) results.get(0);
            float lat = (float) Double.parseDouble((String) bestResult.get("lat"));
            float lng = (float) Double.parseDouble((String) bestResult.get("lon"));
            return new Location(lat, lng);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        // Return if an invalid address was passed through
        return new Location(0, 0);
    }

    /**
     * Gets the address from a given latitude and longitude.
     * @param lat - the given latitude.
     * @param lng - the given longitude.
     * @return the address as a string.
     */
    public String getAddressFromLocation(float lat, float lng) {
        try {
            // Creating the http request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(
                    URI.create("https://nominatim.openstreetmap.org/reverse?format=geocodejson&lat=" + lat + "&lon=" + lng)
            ).build();
            // Getting the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parsing response to get desired details
            JsonObject jsonObject = new JsonParser().parse(response.body()).getAsJsonObject();
            // Access features
            JsonArray features = (JsonArray) jsonObject.get("features");
            // Access the first element of the features arrayList
            JsonElement one = (features.get(0));
            // Access properties
            JsonElement properties = one.getAsJsonObject().get("properties");
            // Access Geocoding
            JsonElement geocoding = properties.getAsJsonObject().get("geocoding");
            // Finally, get address as JsonElement
            JsonElement addressElement = geocoding.getAsJsonObject().get("label");
            // Convert from JsonElement to String
            String addressToString = addressElement.toString();

            // Format string
            String address = addressToString.replace("\"", "");
            String[] addressElements = address.split(",");
            String result;
            if (addressElements.length == 2) {
                result  = addressElements[0] + addressElements[1];
            } else {
                result  = addressElements[0] + addressElements[1] + addressElements[2];
            }
            return result;

        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        // Return if an invalid address was passed through
        return "";
    }

    /**
     * Finds the closest emergency service from a given latitude and longitude.
     * @param service - the service you wish you find (one of: "hospitals", "police" or "fire_station")
     * @param lat - the users current latitude.
     * @param lng - the users current longitude.
     * @return the closest hospital or police station or fire station  as an EmergencyService.
     */
    public EmergencyService searchNearestEmergencyService(String service, float lat, float lng) {
        try {

            // Creating the http request
            String query = service + lat + "," + lng;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(
                    URI.create("https://nominatim.openstreetmap.org/search?&q=" + query + "&format=json")
            ).build();

            // Getting the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parsing the response
            JSONParser parser = new JSONParser();
            JSONArray results = (JSONArray)  parser.parse(response.body());
            JSONObject bestResult = (JSONObject) results.get(0);
            float Servicelat = (float) Double.parseDouble((String) bestResult.get("lat"));
            float Servicelng = (float) Double.parseDouble((String) bestResult.get("lon"));
            String ServiceName = (String) bestResult.get("display_name");
            ServiceName = ServiceName.split(",")[0];

            return new EmergencyService(ServiceName, Servicelat, Servicelng);

        } catch (Exception e) {
            return null;

        }
    }

    /**
     * Will find the closest hospital, has to be "hospitals" as hospitals are tagged as "hospitals".
     * Decided to make three different function for each emergency service as the search tag is very specific
     * so it's easier to have it coded in only one place.
     * @param lat - the users current latitude.
     * @param lng - the users current longitude.
     * @return the closest hospital as an EmergencyService.
     */
    public EmergencyService findClosestHospital(float lat, float lng) {
        return searchNearestEmergencyService("hospitals", lat, lng);
    }

    /**
     * Will find the closest police station, has to be "police" as police stations are tagged as "police".
     * Decided to make three different function for each emergency service as the search tag is very specific
     * so it's easier to have it coded in only one place.
     * @param lat - the users current latitude.
     * @param lng - the users current longitude.
     * @return the closest police station as an EmergencyService.
     */
    public EmergencyService findClosestPoliceStation(float lat, float lng) {
        return searchNearestEmergencyService("police", lat, lng);
    }

    /**
     * Will find the closest fire station, has to be "fire_station" as fire stations are tagged as "fire_station".\
     * Decided to make three different function for each emergency service as the search tag is very specific
     * so it's easier to have it coded in only one place.
     * @param lat - the users current latitude.
     * @param lng - the users current longitude.
     * @return the closest fire station as an EmergencyService.
     */
    public EmergencyService findClosestFireStation(float lat, float lng) {
        return searchNearestEmergencyService("fire_station", lat, lng);
    }

}






