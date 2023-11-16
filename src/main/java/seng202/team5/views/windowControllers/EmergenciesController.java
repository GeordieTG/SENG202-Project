package seng202.team5.views.windowControllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng202.team5.managers.UserManager;
import seng202.team5.models.data.EmergencyService;
import seng202.team5.managers.Geolocation;
import seng202.team5.views.SupplementaryGUIControllers.Alert;
import seng202.team5.views.SupplementaryGUIControllers.jsConnectorInitCaller;


/**
 * Controller for the emergencies.fxml window.
 */
public class EmergenciesController extends jsConnectorInitCaller {

    /**
     * Hospital name.
     */
    public Text hospitalText;

    /**
     * Fire station name.
     */
    public Text fireStationText;

    /**
     * Police station name.
     */
    public Text policeStationText;


    /**
     * The JavaFX stage for the map
     */
    Stage emergenciesStage = new Stage();


    /**
     * Instance of the Geolocation Class
     */
    final Geolocation geolocator = new Geolocation();

    /**
     * Information about the closest hospital
     */
    EmergencyService hospital;
    /**
     * Information about the closest fire station
     */
    EmergencyService fire;
    /**
     * Information about the closest police station
     */
    EmergencyService police;


    /**
     * Initializes the JavaFX window
     * @param stage Top level container for this window
     */
    public void init(Stage stage) {
        emergenciesStage = stage;

        // Sets the texts to "Loading..." while the thread is being run to retrieve the nearest emergency services
        hospitalText.setText("Loading...");
        policeStationText.setText("Loading...");
        fireStationText.setText("Loading...");

        // Creates a new thread to search for the nearest emergency services using the Geolocation API
        // Updates the emergency services on the page as soon as it is found
        new Thread(() -> {

            //Find the closest Hospital
            hospital = geolocator.findClosestHospital(UserManager.getCurrentLocation().getLatitude(), UserManager.getCurrentLocation().getLongitude());
            if (hospital != null) {
                hospitalText.setText(hospital.getName());
            } else {
                hospitalText.setText("You're Doomed - There are no hospitals nearby");
            }

            // Find the closest Fire Station
            fire = geolocator.findClosestFireStation(UserManager.getCurrentLocation().getLatitude(), UserManager.getCurrentLocation().getLongitude());
            if (fire != null) {
                fireStationText.setText(fire.getName());
            } else {
                fireStationText.setText("You're Doomed - There are no fire stations nearby");
            }

            // Find the closest Police Station
            police = geolocator.findClosestPoliceStation(UserManager.getCurrentLocation().getLatitude(), UserManager.getCurrentLocation().getLongitude());
            if(police != null) {
                policeStationText.setText(police.getName());
            } else {
                policeStationText.setText("You're Doomed - There are no police stations nearby");
            }

        }).start();
    }

    /**
     * Views the hospital for the map to display. Displays popup if they attempt to view with no hospitals
     * nearby
     */
    @FXML
    public void viewHospital() {
        if (hospital != null) {
            addMapInitCommand("zoomInOnLocation");
            Object[] objects = new Object[]{hospital.getLat(), hospital.getLng(), hospital.getName()};
            addMapInitParams(objects);
            startMap();
        } else {
            Alert.noEmergencyServiceFound("hospitals");
        }
    }

    /**
     * Views the fire station for the map to display. Displays popup if they attempt to view with no fire stations
     * nearby
     */
    @FXML
    public void viewFireStation() {
        if (fire != null) {
            addMapInitCommand("zoomInOnLocation");
            Object[] objects = new Object[]{fire.getLat(), fire.getLng(), fire.getName()};
            addMapInitParams(objects);
            startMap();
        } else {
            Alert.noEmergencyServiceFound("fire stations");
        }
    }

    /**
     * Views the police station for the map to display. Displays popup if they attempt to view with no police stations
     * nearby
     */
    @FXML
    public void viewPoliceStation() {
        if (police != null) {
            addMapInitCommand("zoomInOnLocation");
            Object[] objects = new Object[]{police.getLat(), police.getLng(), police.getName()};
            addMapInitParams(objects);
            startMap();
        } else {
            Alert.noEmergencyServiceFound("police stations");
        }
    }
}
