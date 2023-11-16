package seng202.team5.views.windowControllers;

import com.jfoenix.controls.JFXDrawer;
import javafx.animation.PauseTransition;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCombination;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import netscape.javascript.JSObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import seng202.team5.exceptions.NotLoggedInException;
import seng202.team5.managers.*;
import seng202.team5.models.data.*;
import seng202.team5.models.databaseInteraction.Filter;
import seng202.team5.views.SupplementaryGUIControllers.CurrentJourneyObserver;
import seng202.team5.views.SupplementaryGUIControllers.JavaScriptBridge;
import seng202.team5.views.SupplementaryGUIControllers.JourneyDrawerController;
import seng202.team5.views.SupplementaryGUIControllers.jsConnectorInitCaller;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Controller for the mapView.fxml window
 */
public class MapController implements CurrentJourneyObserver {

    /**
     * Checkbox for touristAttraction filter
     */
    public CheckBox touristAttractionCheck;

    /**
     * Checkbox for carParkNeedsPayment filter
     */
    public CheckBox carParkNeedsPaymentCheck;

    /**
     * Checkbox for alwaysOpen filter
     */
    public CheckBox alwaysOpenCheck;
    /**
     * Slider for number of parks at charger
     */
    public Slider numberOfParksSlider;
    /**
     * Slider for maximum time limit
     */
    public Slider maxTimeLimitSlider;
    /**
     * Text field for search bar
     */
    public TextField searchBar;

    /**
     * Checkbox for type 1 tether
     */
    @FXML
    public CheckBox type1TetheredCheckBox;
    /**
     * Checkbox for type 2 socket
     */
    @FXML
    public CheckBox type2SocketedCheckBox;
    /**
     * Checkbox for type 2 ccs
     */
    @FXML
    public CheckBox type2CCSCheckBox;
    /**
     * Checkbox for CHAdeMO
     */
    @FXML
    public CheckBox CHAdeMOCheckBox;
    /**
     * Checkbox for type 2 tether
     */
    @FXML
    public CheckBox type2TetheredCheckBox;

    @FXML
    public Tooltip blackTooltip;
    @FXML
    public Tooltip blueTooltip;
    @FXML
    public Tooltip greenTooltip;
    @FXML
    public Tooltip redTooltip;
    @FXML
    public Tooltip greyTooltip;
    @FXML
    public Tooltip orangeTooltip;

    /**
     * Geolocation used in application
     */
    private final Geolocation geolocator = new Geolocation();


    /**
     * Boolean for if the route is currently on display within the app
     */
    private boolean routeDisplayed = false;

    /**
     * Gives the GUI access to the charger data that is stored in the database
     */
    private final ChargerManager chargerManager = new ChargerManager();

    /**
     * JavaFX stage for the map
     */
    private Stage mapStage = new Stage();



    /**
     *  The view component that contains the map
     */
    @FXML
    private WebView mapWebView;

    /**
     * The fxml drawer UI for saving journeys
     */
    @FXML
    JFXDrawer journeyDrawer;

    /**
     * The engine is the primary connection between the Application and the Javascript that interacts with the map
     */
    private WebEngine mapWebEngine = null;

    /**
     * The Javascript object within the leaflet_osm_map.html that controls the Javascript of the map
     * jsConnector.call("exampleFunction") is how we call Javascript functions from Java
     */
    private JSObject jsConnector;

    /**
     * Bridge used to connect the Javascript programming of the map to the Java code written here
     */
    private final JavaScriptBridge javaScriptBridge = new JavaScriptBridge();

    /**
     * Filter Manager is used to receive a list of filter objects
     */
    private final FilterManager filterManager = new FilterManager();

    /**
     * As done in the advanced FX Lab
     * Turns the HTML file into a string to be passed
     * @return A string representing the HTML file for the Map view
     */
    private String getLeafletOSMMapHTML() {
        InputStream is = getClass().getResourceAsStream("/html/leaflet_osm_map.html");
        assert is != null;
        return new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    /**
     * Initialises the map by loading the html into the webengine and allocating the JSController
     */
    private void initMap(jsConnectorInitCaller jsInitCaller) {
        mapWebEngine = mapWebView.getEngine();
        mapWebEngine.setJavaScriptEnabled(true);
        mapWebEngine.loadContent(getLeafletOSMMapHTML());

        mapWebEngine.getLoadWorker().stateProperty().addListener(
                (ov, oldState, newState) -> {
                    // if javascript loads successfully
                    if (newState == Worker.State.SUCCEEDED) {
                        // set our bridge object
                        JSObject window = (JSObject) mapWebEngine.executeScript("window");
                        window.setMember("javaScriptBridge", javaScriptBridge);
                        // get a reference to the js object that has a reference to the js methods wetest need to use in java
                        jsConnector = (JSObject) mapWebEngine.executeScript("jsConnector");
                        // call the javascript function to initialise the map
                        jsConnector.call("initMap", UserManager.getCurrentLocation().getLatitude(), UserManager.getCurrentLocation().getLongitude());
                        jsConnector.call("setCurrentLocation", UserManager.getCurrentLocation().getLatitude(), UserManager.getCurrentLocation().getLongitude());
                        if (jsInitCaller != null) {
                            runJSConnectorInitialCommands(jsInitCaller);
                        }
                        try {
                            addChargersOnMap();
                        } catch (NotLoggedInException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    /**
     * Called from the Plan Journey Button
     * Simple toggle to hide or display the route on click
     */
    public void toggleRoute() {
        if(routeDisplayed){
            removeRoute();
            closeJourneyDrawer();
        } else {
            showJourneyPlanning();
        }
    }

    /**
     * Runs the functionality to open the window and inject the journey planner into it
     */
    private void openJourneyDrawer() {
       try {
           mapStage.setFullScreen(true);
           FXMLLoader journeyDrawerLoader = new FXMLLoader(getClass().getResource("/fxml/journeyDrawer.fxml"));
           Parent journeyDrawerParent = journeyDrawerLoader.load();
           JourneyDrawerController journeyDrawerController = journeyDrawerLoader.getController();
           journeyDrawerController.init(this, mapStage);
           journeyDrawer.setSidePane(journeyDrawerParent);
           journeyDrawer.open();
           showNewRoute(Journey.getCurrentJourneySingleton().getStops());
       } catch (Exception e) {
           e.printStackTrace();
       }
    }


    /**
     * Closes the Journey Drawer
     */
    private void closeJourneyDrawer() {

        mapStage.setFullScreen(false);
        journeyDrawer.close();
    }

    /**
     * Helper function for toggle route:
     * sets the route to be shown and adds a route to the javascript
     */
    private void showJourneyPlanning() {
        routeDisplayed = true;
        jsConnector.call("addRoute", new JSONArray(Journey.getCurrentJourneySingleton().getStops()));
        openJourneyDrawer();
    }

    /**
     * Shows the new route on update
     * @param locations coordinates of stops
     */
    public void showNewRoute(List<Location> locations) {
        jsConnector.call("addRoute", new JSONArray(locations));
    }

    /**
     * Helper function for Toggle Route:
     * removes route from map through javascript and changes routeDisplayed to match state
     */
    private void removeRoute () {
        routeDisplayed = false;
        jsConnector.call("removeRoute");
    }

    /**
     * As taken from Advanced FX Lab
     * Gets the list of chargers from the database through the chargerManager and if the Lat lng isn't 0,0 displays them
     * on the map
     */
    private void addChargersOnMap() throws NotLoggedInException {
        List<Charger> chargers = chargerManager.getAllChargers();
        for (Charger charger: chargers){
            if (charger != null && charger.getLocation().getLatitude()!= 0f && charger.getLocation().getLongitude() != 0f) {
                addChargerMarker(charger);
            }
        }
    }

    /**
     * Displays an individual charger on the map
     * @param charger The charger to display
     */
    private void addChargerMarker (Charger charger) throws NotLoggedInException {
        int favourite = 0;
        int loggedin;


        if (UserManager.getIsUserLoggedIn()) {
            loggedin = 1;
            if (UserManager.getCurrentlyLoggedIn().getFavouriteChargerLocationList().contains(charger.getLocation())) {
                favourite = 1;
            }
        } else {
            loggedin = 0;
        }

        jsConnector.call("addMarker", charger.toMarkerString(), charger.getLocation().getLatitude(), charger.getLocation().getLongitude(), loggedin, favourite);
    }

    /**
     * Displays a list of chargers on the map
     * @param chargers List of chargers to display
     */
    public void addChargerMarkers (ArrayList<Charger> chargers) throws NotLoggedInException {
        for (Charger charger: chargers){
            if (charger != null && charger.getLocation().getLatitude()!= 0f && charger.getLocation().getLongitude() != 0f) {
                addChargerMarker(charger);
            }
        }
    }

    /**
     * Initialize the JavaFX window
     * @param stage Top level container for this window
     * @param jsInitCaller Given the map is called from somewhere that wants to apply a function to the map, the
     *                     caller is passed through here to allow for the methods to be grabbed and called
     *                     after the map has been initialised
     */
    public void init(Stage stage, jsConnectorInitCaller jsInitCaller) {
        mapStage = stage;
        initMap(jsInitCaller);

        blackTooltip.setShowDelay(Duration.millis(100));
        blueTooltip.setShowDelay(Duration.millis(100));
        greenTooltip.setShowDelay(Duration.millis(100));
        redTooltip.setShowDelay(Duration.millis(100));
        greyTooltip.setShowDelay(Duration.millis(100));
        orangeTooltip.setShowDelay(Duration.millis(100));

    }

    /**
     * Runs the initial JSConnector commands as specified by the jsInitCaller
     * @param jsInitCaller The class that called the map view and allows us to specify the commands to run on startup
     */
    private void runJSConnectorInitialCommands(jsConnectorInitCaller jsInitCaller) {
        List<String> initCommands = jsInitCaller.getJSConnectorFunctions();
        ArrayList<Object[]> initParams = jsInitCaller.getFunctionArguments();
        for (int i = 0; i < initCommands.size(); i++) {
            if (initCommands.get(i).equals("addRoute")) {
                showJourneyPlanning();
                return;
            }
            jsConnector.call(initCommands.get(i), initParams.get(i));
        }
    }

    /**
     * Called when user clicks "Apply" in the filter's dropdown
     * Takes in what options user has selected and displays chargers on map that meet the filter criteria
     */
    public void filterChargers() throws NotLoggedInException {
        // Remove all markers
        jsConnector.call("clearMarkers");

        // Pass the button selections to filterManger to receive a list of filter objects
        ArrayList<Filter> filters = filterManager.filter(alwaysOpenCheck, carParkNeedsPaymentCheck, touristAttractionCheck, numberOfParksSlider, maxTimeLimitSlider, type1TetheredCheckBox, type2SocketedCheckBox, type2CCSCheckBox, type2TetheredCheckBox, CHAdeMOCheckBox);

        // Pass the list of filters to the filter function and receive a list of chargers that meet the criteria
        ArrayList<Charger> chargers = chargerManager.filter(filters);

        // From the list of filtered chargers, add them to the map
        addChargerMarkers(chargers);
    }


    /**
     * Clears the applied filters and places all charger markers back on the map
     */
    public void clearFilters() throws NotLoggedInException {

        // Remove all markers
        jsConnector.call("clearMarkers");

        // Reset Check Boxes
        alwaysOpenCheck.setSelected(false);
        carParkNeedsPaymentCheck.setSelected(false);
        touristAttractionCheck.setSelected(false);

        type2TetheredCheckBox.setSelected(false);
        type1TetheredCheckBox.setSelected(false);
        type2CCSCheckBox.setSelected(false);
        type2SocketedCheckBox.setSelected(false);
        CHAdeMOCheckBox.setSelected(false);

        // Reset Sliders
        maxTimeLimitSlider.setValue(0);
        numberOfParksSlider.setValue(0);

        // Add all chargers back to the map
        ArrayList<Charger> chargers = chargerManager.getAllChargers();
        addChargerMarkers(chargers);
    }

    /**
     * Uses the GeoLocation class to take the address entered into the search bar
     * and calls the jsConnector to zoom in on the that address on the map
     */
    public void searchLocation() {

        if (!searchBar.getText().isBlank()) {
            Location location = geolocator.queryAddress(searchBar.getText());

            // When a location can't be found from the given address
            if (location.getLatitude() == 0 & location.getLongitude() == 0) {
                addressSearchInvalid();

            } else {
                jsConnector.call("zoomInOnLocation", location.getLatitude(), location.getLongitude(), searchBar.getText());
            }
        } else {
            addressSearchInvalid();
        }
    }

    /**
     * Changes the style of the search bar to red for 3 seconds
     */
    public void addressSearchInvalid() {
        searchBar.setStyle("-fx-text-box-border: red;");

        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3)
        );
        visiblePause.setOnFinished(
                event -> searchBar.setStyle("")
        );
        visiblePause.play();
    }

    /**
     * Called when a journey is updated
     */
    @Override
    public void handleJourneyUpdate() {
        if (routeDisplayed) {
            removeRoute();
            showJourneyPlanning();
        }
    }

    /**
     * Zooms into the emergency services
     * @param lat latitude of emergency service
     * @param lng longitude of emergency service
     * @param title title of emergency service
     */
    public void zoomToEmergencyService(float lat, float lng, String title) {
        jsConnector.call("addEmergencyService", lat, lng, title);
    }

}

