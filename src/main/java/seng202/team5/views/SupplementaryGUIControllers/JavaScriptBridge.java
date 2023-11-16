package seng202.team5.views.SupplementaryGUIControllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import seng202.team5.exceptions.NoRouteException;
import seng202.team5.exceptions.NotFoundException;
import seng202.team5.exceptions.NotLoggedInException;
import seng202.team5.managers.ChargerManager;
import seng202.team5.managers.Geolocation;
import seng202.team5.managers.UserManager;
import seng202.team5.models.data.*;
import seng202.team5.views.windowControllers.MainController;
import seng202.team5.views.windowControllers.ViewChargerController;

/**
 * Simple example class showing the ability to 'bridge' from javascript to java
 * The functions within can be called from our javascript in the map view when we set an object of this class
 * as a member of the javascript
 * Note: This is a very basic example you can use any java code, though you may need to be careful when working
 * with objects
 */
public class JavaScriptBridge {

    /**
     * Array List holding instructions for the current route
     */
    private static ArrayList<String> currentRouteInstructions = null;

    /**
     * Instance of charger manager for the application
     */
    private final ChargerManager chargerManager = new ChargerManager();

    /**
     * User rating for a given charger
     */
    public int rating;

    /**
     * Instance of user manager for the application
     */
    private final UserManager userManager = new UserManager();

    /**
     * Calls the application logger
     */
    private static final Logger log = LogManager.getLogger();

    /**
     * Getter for the current set of instructions for the given route
     * @return An Array List of the current route instructions
     */
    public static ArrayList<String> getCurrentRouteInstructions() throws NoRouteException{
        if (currentRouteInstructions == null) {
            throw new NoRouteException("No route to display");
        } else {
            return currentRouteInstructions;
        }
    }

    /**
     * Whenever a new route is generated, this function receives an updated JSONArray of instructions for the driver
     * @param routeInformation JSONArray representing the different instructions
     */
    public void saveRouteInfo(String routeInformation) {
        try {
            JSONArray instructionArray = new JSONArray(routeInformation);
            ArrayList<String> instructions = new ArrayList<>();
            for (int i = 0; i < instructionArray.length(); i++) {
                JSONObject jsonobject = instructionArray.getJSONObject(i);
                instructions.add(jsonobject.getString("text"));
            }
            currentRouteInstructions = instructions;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Gets the route coordinates for each marker in route and stores in an ArrayList of locations
     * representing the journey
     * @param coords An ArrayList turned string that contains all the markers for the journey and their locations.
     * @throws JSONException the string that has been passed through is not JSON.
     */
    public void representRoute(String coords, String distance) throws JSONException {

        double distanceRoute = Double.parseDouble(distance);
        float CO2Emitted = (float) (((distanceRoute/1000) * 75)/1000);

        ArrayList<Location> currentRouteCoords = new ArrayList<Location>();
        JSONArray coordArray = new JSONArray(coords);

        for (int i = 0; i < coordArray.length(); i++) {
            JSONObject marker = coordArray.getJSONObject(i);

            double lng = marker.getDouble("lng");
            double lat = marker.getDouble("lat");

            Location newLocation = new Location((float)lat, (float)lng);

            if (!currentRouteCoords.contains(newLocation)) {
                currentRouteCoords.add(newLocation);
            }
        }
        Journey currentJourney = new Journey(Journey.getCurrentJourneySingleton().getTitle(), currentRouteCoords, CO2Emitted);
        if (!currentJourney.equals(Journey.getCurrentJourneySingleton())) {
            Journey.setCurrentJourneySingleton(currentJourney);
        }
    }

    /**
     * Will add a charger from its given lat and lng to the users favourite
     * charger list.
     * @param lat the latitude of the charger.
     * @param lng the longitude of the charger.
     * @throws NotLoggedInException if the user is not logged in.
     */
    public void addChargerToFavourites(float lat, float lng) throws NotLoggedInException {
        if (UserManager.getIsUserLoggedIn()) {
            Location location = new Location(lat,lng);
            if (!UserManager.getCurrentlyLoggedIn().getFavouriteChargerLocationList().contains(location)) {
                UserManager.getCurrentlyLoggedIn().addToFavouriteChargerLocationList(location);
            }
        } else {
            Alert.notLoggedInAlert();
        }
    }

    /**
     * Views a charger when the "View Charger" Button is Pressed
     * @param lat The latitude of the charger
     * @param lng The longitude of the charger
     */
    public void viewCharger(float lat, float lng) throws NotFoundException, SQLException {

        ChargerManager chargers = new ChargerManager();
        Charger charger = chargers.getFromLatLong(lat, lng);

        ViewChargerController.isMapStage = true;
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openViewCharger(charger);
    }

    /**
     * Will set the rating with a given number.
     * @param num the desired rating.
     */
    public void setRating(int num) {
        this.rating = num;
    }

    /**
     * Will get the rating.
     * @return the rating as an integer.
     */
    public int getRate() {
        return rating;
    }

    /**
     * Removes the charger from the users favourite charger list that matches the passed through latitude and longitude
     * @param lat latitude of the charger wanting to be removed
     * @param lng longitude of the charger wanting to be removed
     * @throws NotLoggedInException when the user is not logged in.
     */
    public void removeChargerFromFavourites(double lat, double lng) throws NotLoggedInException {
        if (UserManager.getIsUserLoggedIn()) {
            Charger charger = null;
            try {
                charger = chargerManager.getFromLatLong((float)lat, (float)lng);
            } catch (SQLException | NotFoundException e) {
                e.printStackTrace();
            }
            assert charger != null;
            UserManager.getCurrentlyLoggedIn().removeChargerFromFavourites(charger);
        } else {
            Alert.notLoggedInAlert();
        }
    }

    /**
     * Sets the users current location to the lat/long clicked on the map
     */
    public void addCurrentLocation(float lat, float lng) {
        // Set users current location as passed lat/long
        Journey.resetJourneySingleton();
        userManager.setCurrentLocation(new Location(lat,lng));
        // Set the current location label to reverse geolocated address
        Geolocation geolocation = new Geolocation();
        String address = geolocation.getAddressFromLocation(lat, lng);

        MainController.getMostRecentMainController().setLocationLabel(address);
    }

}