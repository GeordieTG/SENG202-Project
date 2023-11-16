package seng202.team5.views.SupplementaryGUIControllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team5.exceptions.IncorrectConfigForEnvException;
import seng202.team5.exceptions.NoRouteException;
import seng202.team5.exceptions.NotLoggedInException;
import seng202.team5.managers.EmailManager;
import seng202.team5.managers.Geolocation;
import seng202.team5.managers.UserManager;
import seng202.team5.models.data.*;
import seng202.team5.views.windowControllers.MapController;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Contains the functionality of the Journey Drawer that is accessible from within the map screen
 */
public class JourneyDrawerController implements CurrentJourneyObserver {

    /**
     * Reference to the map to allow the JourneyDrawer to interact with the map that is displayed
     */
    MapController mapController;

    /**
     * The stage to be prepared
     */
    Stage stage;



    /**
     * The ListView
     */
    @FXML
    ListView<String> routeMarkerList;

    /**
     * The label for CO2 Emitted for current journey
     */
    @FXML
    Label CO2EmittedLabel;

    /**
     * The text field for inputting addresses
     */
    @FXML
    TextField addressTextField;

    /**
     * The button that adds an address to journey
     */
    @FXML
    Button addAddressButton;

    /**
     * The button for the current journey to be emailed
     */
    @FXML
    Button emailButton;

    /**
     * The button for saving journeys in user
     */
    @FXML
    Button saveJourneyButton;

    /**
     * The text field for naming journeys
     */
    @FXML
    TextField nameJourneyText;

    /**
     * Label for alerts or errors
     */
    @FXML
    Label alertLabel;

    /**
     * Most recent controller for Threads to reach
     */
    private static JourneyDrawerController mostRecentController;

    /**
     * The current locations
     */
    final Map<String, Location> cachedLocations = new HashMap<String, Location>();

    /**
     * Calls the application logger
     */
    private static final Logger log = LogManager.getLogger();

    /**
     * Clears the alert label
     */
    public static void clearLabel() {
        mostRecentController.alertLabel.setText("");
    }

    /**
     * Sets the label of the journey drawer to say "Sent!" when the email is successfully sent
     */
    public static void setLabelToSentEmail() {
        mostRecentController.alertLabel.setText("Sent!");
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3)
        );
        visiblePause.setOnFinished(
                event -> JourneyDrawerController.clearLabel()
        );
        visiblePause.play();
    }

    /**
     * Sets the reference to the map controller that is needed for the JourneyDrawer to interact with the map
     * @param mapController The current map controller
     */
    public void init(MapController mapController, Stage stage) {
        this.stage = stage;
        mostRecentController = this;
        Journey.addCurrentJourneyObserver(this);
        this.mapController = mapController;
        handleJourneyUpdate();
        updateCO2();

        if (UserManager.getIsUserLoggedIn()) {
            emailButton.setVisible(true);
            saveJourneyButton.setVisible(true);
        } else {
            emailButton.setVisible(false);
            saveJourneyButton.setVisible(false);
        }

        routeMarkerList.getItems().addListener((ListChangeListener<? super String>) change -> {
            ArrayList<Location> stops = new ArrayList<Location>();
            List<String> items = routeMarkerList.getItems();
            for (String location : items) {
                stops.add(cachedLocations.get(location));
            }
            mapController.showNewRoute(stops);
            updateCO2();
         });
    }

    /**
     * Updates the CO2Emitted Label for every marker added
     */
    public void updateCO2() {
        Journey current = Journey.getCurrentJourneySingleton();
        CO2EmittedLabel.setText((String.valueOf(current.getCO2Emitted())));
        CO2EmittedLabel.setVisible(true);
    }

    /**
     * Given that the user has an address within the search bar, the address will be queried in the geolocation and be added
     * to the list of stops for the user
     */
    public void addAddressToStops() {
        if (!addressTextField.getText().isBlank()) {
            Geolocation geolocation = new Geolocation();
            try {
                Location newLocation = geolocation.queryAddress(addressTextField.getText());
                String address = geolocation.getAddressFromLocation(newLocation.getLatitude(), newLocation.getLongitude());
                cachedLocations.put(address, newLocation);
                routeMarkerList.getItems().add(address);
                addressTextField.setText("Address to add");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Shows the locations of the stops of the current journey.
     */
    @Override
    public void handleJourneyUpdate() {
        nameJourneyText.setText(Journey.getCurrentJourneySingleton().getTitle());
        Geolocation geolocation = new Geolocation();
        ObservableList<String> displayList = routeMarkerList.getItems();
        ArrayList<String> locationStrings = new ArrayList<String>();
        for (Location location : Journey.getCurrentJourneySingleton().getStops()) {
            String locationString = geolocation.getAddressFromLocation(location.getLatitude(), location.getLongitude());
            cachedLocations.put(locationString, location);
            locationStrings.add(locationString);
        }
        displayList.setAll(locationStrings);
        routeMarkerList.setItems(displayList);
        routeMarkerList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override public ListCell<String> call(ListView<String> list) {
                return new JourneyStopListCell();
            }
        });
    }

    /**
     * Will send a screenshot of the current journey to the current users email.
     */
    public void sendJourneyToEmail() {
        WritableImage writableImage = stage.getScene().snapshot(null);
        File file = new File("capturedScene.png");
        alertLabel.setText("Sending Email...");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
            emailExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EmailManager.sendRouteToUser(file.getAbsolutePath());
                    } catch (MessagingException | IncorrectConfigForEnvException e) {
                        Platform.runLater(Alert::emailServiceDown);
                    } catch (NotLoggedInException e) {
                        Platform.runLater(Alert::notLoggedInAlert);
                    } catch (NoRouteException e) {
                        Platform.runLater(Alert::noRouteAlert);
                    }
                }
            });
            emailExecutor.shutdown(); // it is very important to shut down your non-singleton ExecutorService.
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Updates the journeys name whenever the user updates the text box representing the journeys name and warns the user
     * if the name is invalid
     */
    public void updateJourneyName() {
        if (!nameJourneyText.getText().isBlank()) {
            Journey.getCurrentJourneySingleton().setTitle(nameJourneyText.getText());
            nameJourneyText.setStyle("");
            alertLabel.setText("");
        } else {
            nameJourneyText.setStyle("-fx-text-box-border: red;");
            alertLabel.setText("The Journey needs a name!");
        }
    }

    /**
     * Saves the journey to the users account to view it later
     */
    public void saveJourneyToUser() {
        if (nameJourneyText.getText().isBlank()) {
            Alert.noJourneyName();
            return;
        }

        try {
            User loggedInUser = UserManager.getCurrentlyLoggedIn();
            loggedInUser.addJourney(Journey.getCurrentJourneySingleton());
            alertLabel.setText("Journey Saved!");
        } catch (IllegalArgumentException e) {
            alertLabel.setText("Duplicate Journey name, please change name");
        } catch (Exception e) {
            log.error(e.getMessage()); // User has to be logged in to see the button to call this function
        }
    }


}
