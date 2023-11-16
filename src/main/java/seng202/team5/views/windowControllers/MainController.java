package seng202.team5.views.windowControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.stage.Stage;
import javafx.scene.Parent;
import seng202.team5.exceptions.NotLoggedInException;
import seng202.team5.managers.ChargerManager;
import seng202.team5.managers.UserManager;
import seng202.team5.models.csvInteraction.ChargerCSVReader;
import seng202.team5.models.data.Charger;
import seng202.team5.models.data.Connector;
import seng202.team5.models.data.User;
import seng202.team5.views.SupplementaryGUIControllers.Alert;
import seng202.team5.views.SupplementaryGUIControllers.jsConnectorInitCaller;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller for the main.fxml window.
 * Functionality of the sidebar that is consistent throughout the app.
 * Adapted from Morgan English Advanced JavaFX lab.
 */
public class MainController {

    /**
     * MainControllers singleton.
     */
    private static MainController mostRecentMainController;

    /**
     * Displays the users current location as a text field on the sidebar
     */
    @FXML
    public Text currentLocationTextLine1;
    @FXML
    public Text currentLocationTextLine2;
    @FXML
    public Text currentLocationTextLine3;

    /**
     * Function that returns the most recent main controller.
     * @return - The current MainController.
     */
    public static MainController getMostRecentMainController() {
        return mostRecentMainController;
    }

    /**
     * Boolean to represent if the base dataset has been added to the database
     */
    private static boolean loadedStartingData = false;

    /**
     * Calls the application logger
     */
    private static final Logger log = LogManager.getLogger();

    /**
     * Border pane holding the current window
     */
    @FXML
    private BorderPane mainWindow;

    /**
     * Button to allow users to login
     */
    public Button loginButton;

    /**
     * Button to allow users to logout
     */
    public Button logOutButton;

    /**
     * Text to notify users of login status
     */
    @FXML
    public Text userLoggedInText;

    /**
     * Button for the Register Vehicle
     */
    public Button registerVehicleButton;

    /**
     * Instance of current stage in use
     */
    private Stage stage;

    /**
     * Initializes the controller into the stage
     * @param stage The current stage being used
     * @param page the view that we want to go to
     */
    void init(Stage stage, int page) {
        mostRecentMainController = this;
        if (!loadedStartingData) {
            loadedStartingData = true;
            try {
                ArrayList<Charger> chargerList = new ChargerCSVReader().readAllChargers("BaseData");
                new ChargerManager().addBatch(chargerList);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        this.stage = stage;
        User currentUser = null;
        try {
            currentUser = UserManager.getCurrentlyLoggedIn();
        } catch (NotLoggedInException e) {
            log.warn(e.getMessage());
        }

        if (currentUser != null) {
            logOutButton.setVisible(true);
            String username = currentUser.getUsername();
            userLoggedInText.setText("Logged in as " + username);
        } else {
            loginButton.setVisible(true);
        }

        if (page == 0) {
            openMapWindow(null);
        } else if (page == 1) {
            openTableWindow();
        } else if (page == 2) {
            loadGeneralProfileView(stage);
        }

        stage.sizeToScene();
    }

    /**
     * Sets the "Current Location" Label on the bottom left of the screen
     * @param address users current location as string
     */
    public void setLocationLabel(String address) {

        String[] addressElements = address.split(" ");

        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();

         int currentLine = 1;

        for (int i=0; i<addressElements.length; i++) {

            switch (currentLine) {

                case 1:
                    if (line1.length() + addressElements[i].length() < 28) {
                        if (line1.length() == 0) {
                            line1.append(addressElements[i]);
                        } else {
                            line1.append(" ").append(addressElements[i]);
                        }
                    } else {
                        line2.append(addressElements[i]);
                        currentLine = 2;
                    }
                    break;
                case 2:
                    if (line2.length() + addressElements[i].length() < 28) {
                        if (line2.length() == 0) {
                            line2.append(addressElements[i]);
                        } else {
                            line2.append(" ").append(addressElements[i]);
                        }
                    } else {
                        line3.append(addressElements[i]);
                        currentLine = 3;
                    }
                    break;
                case 3:

                    if (line3.length() > 28) {
                        break;
                    }
                    if (line3.length() == 0) {
                        line3.append(addressElements[i]);
                    } else {
                        line3.append(" ").append(addressElements[i]);
                    }
                    break;
            }
            if (line3.length() > 28) {
                break;
            }
        }

        currentLocationTextLine1.setText(line1.toString());
        currentLocationTextLine2.setText(line2.toString());
        currentLocationTextLine3.setText(line3.toString());

        if (address.isBlank()) {
            currentLocationTextLine1.setText("Swimming with the fish");
        }
    }

    /**
     * Calls Map View either from the GUI through openMapWindow() or through other windows
     * @param caller Given that the map is opened from another window the caller passes reference to that
     *               window which has stored the commands that it wishes to run on the jsConnector
     */
    public void openMapWindow(jsConnectorInitCaller caller) {
        loadMapView(stage, caller);
        stage.setResizable(true);
    }

    /**
     * Opens the new charger screen
     */
    public void openNewChargerWindow() {
        stage.setFullScreen(false);
        loadNewChargerView(stage);
        stage.setResizable(false);
        stage.setHeight(650.0);
        stage.setWidth(900.0);
    }

    /**
     *
     * @param connector the connector to be edited
     * @param charger the charger that the connector is attached to
     */
    public void openEditConnectors(Connector connector, Charger charger) {
        stage.setFullScreen(false);
        loadEditConnectors(stage, connector, charger);
        stage.setResizable(false);
        stage.setHeight(650.0);
        stage.setWidth(900.0);
    }

    /**
     * Function called by the GUI
     */
    @FXML
    void openMapWindow() {
        openMapWindow(null);
    }

    /**
     * Calls Table View when the table button is pressed
     */
    @FXML
    void openTableWindow() {
        stage.setFullScreen(false);
        loadTableView(stage);
        stage.setResizable(true);
    }

    /**
     * Calls the Emergency View when the emergencies button is pressed
     */
    @FXML
    void openEmergenciesWindow() {
        stage.setFullScreen(false);
        loadEmergenciesView(stage);
        stage.setResizable(false);
        stage.setHeight(650.0);
        stage.setWidth(900.0);
    }

    /**
     * Calls Register Vehicle view when the register vehicle button is pressed
     */
    @FXML
    void openRegisterVehicleWindow() {
        stage.setFullScreen(false);
        UserManager userManager = new UserManager();
        User user = null;
        try {
            user = UserManager.getCurrentlyLoggedIn();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        if (user != null) {
            loadRegisterVehicleView(stage);
            stage.setResizable(false);
            stage.setHeight(650.0);
            stage.setWidth(900.0);
        } else {
            Alert.notLoggedInAlert();
        }
    }

    /**
     * Calls the Previous Journeys view when the previous journeys button is pressed
     */
    @FXML
    void openPreviousJourneysWindow() {
        stage.setFullScreen(false);
        UserManager userManager = new UserManager();
        User user = null;
        try {
            user = UserManager.getCurrentlyLoggedIn();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        if (user != null) {
            loadPreviousJourneys(stage);
            stage.setResizable(false);
            stage.setHeight(650.0);
            stage.setWidth(900.0);
        } else {
            Alert.notLoggedInAlert();
        }
    }

    /**
     * Calls Settings view when the settings button is pressed
     */
    @FXML
    void openSettingsWindow() {
        stage.setFullScreen(false);
        loadGeneralProfileView(stage);
        stage.setResizable(false);
        stage.setHeight(650.0);
        stage.setWidth(900.0);
    }

    /**
     * Calls Help view when the help button is pressed
     */
    @FXML
    void openHelpWindow() {
        stage.setFullScreen(false);
        loadHelpView(stage);
        stage.setResizable(false);
        stage.setHeight(650.0);
        stage.setWidth(900.0);
    }

    /**
     * Calls Login view when the login button is pressed
     */
    @FXML
    void openLogin() {
        stage.setFullScreen(false);
        loadLogin(stage);
        stage.setResizable(false);
    }

    /**
     * Calls logout view when logout button is pressed
     */
    @FXML
    void logOut() {
        stage.setFullScreen(false);
        UserManager.activeUserLogOut();
        loadLogin(stage);
        stage.setResizable(false);
    }

    /**
     * Calls to load the favourite chargers screen when the button is pressed.
     */
    @FXML
    void openFavouriteChargers() {
        stage.setFullScreen(false);
        loadFavouriteChargers(stage);
        stage.setResizable(false);
        stage.setHeight(650.0);
        stage.setWidth(900.0);
    }

    /**
     * Calls to load the view profile screen when the button is pressed.
     */
    @FXML
    void openViewProfile() {
        stage.setFullScreen(false);
        loadViewProfile(stage);
        stage.setResizable(false);
        stage.setHeight(650.0);
        stage.setWidth(900.0);
    }

    /**
     * Calls to load the edit profile screen when the button is pressed.
     */
    void openEditProfile() {
        stage.setFullScreen(false);
        loadEditProfile(stage);
        stage.setResizable(false);
        stage.setHeight(650.0);
        stage.setWidth(900.0);
    }

    /**
     * Calls to load the vehicle screen when the button is pressed.
     */
    void openVehiclesWindow() {
        stage.setFullScreen(false);
        loadVehicleProfile(stage);
        stage.setResizable(false);
        stage.setHeight(650.0);
        stage.setWidth(900.0);
    }

    /**
     *  Will call to load the view charger screen, also sets the stage to non-resizable.
     * @param charger - The charger that the user wishes to load.
     */
    public void openViewCharger(Charger charger) {
        stage.setFullScreen(false);
        loadViewCharger(charger, stage);
        stage.setResizable(false);
        stage.setHeight(650.0);
        stage.setWidth(900.0);
    }

    /**
     * Will call to load the view connectors screen, also sets the stage to non-resizable.
     * @param charger - The charger that the user wishes to load.
     */
    public void openViewConnectors(Charger charger) {
        stage.setFullScreen(false);
        loadViewConnectors(charger, stage);
        stage.setResizable(false);
        stage.setHeight(650.0);
        stage.setWidth(900.0);
    }

    /**
     * Will call to load the edit charger screen, also sets the stage to non-resizable.
     * @param charger - The charger that the user wishes to edit.
     */
    public void openEditCharger(Charger charger) {
        stage.setFullScreen(false);
        loadEditCharger(charger, stage);
        stage.setResizable(false);
        stage.setHeight(650.0);
        stage.setWidth(900.0);
    }

    /**
     * Injects the view charger into the current stage.
     * @param charger - The charger that the user wishes to view.
     * @param stage - The current stage.
     */
    private void loadViewCharger(Charger charger, Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader chargerViewLoader = new FXMLLoader(getClass().getResource("/fxml/viewCharger.fxml"));
            Parent chargerViewParent = chargerViewLoader.load();
            ViewChargerController viewChargerController = chargerViewLoader.getController();
            viewChargerController.init(charger);

            mainWindow.setCenter(chargerViewParent);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Gets the JavaFX Stage
     * @return Returns the stage of the application
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Injects the new charger screen into the current stage
     * @param stage the current stage
     */
    public void loadNewChargerView(Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader newChargerLoader = new FXMLLoader(getClass().getResource("/fxml/newCharger.fxml"));
            Parent newChargerParent = newChargerLoader.load();
            NewChargerController newChargerController = newChargerLoader.getController();
            newChargerController.init(stage);
            mainWindow.setCenter(newChargerParent);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Injects the edit connectors screen into the current stage.
     * @param stage - The current stage.
     * @param connector - The connector that the user wishes to edit.
     * @param charger - The charger that the connectors are connected to.
     */
    private void loadEditConnectors(Stage stage, Connector connector, Charger charger) {
        stage.setFullScreen(false);
        try {
            FXMLLoader editConnectorsLoader = new FXMLLoader(getClass().getResource("/fxml/editConnectors.fxml"));
            BorderPane editChargerParent = editConnectorsLoader.load();
            EditConnectorsController editConnectorController = editConnectorsLoader.getController();
            editConnectorController.init(connector, charger);
            mainWindow.setCenter(editChargerParent);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Will inject the edit charger screen into the current stage.
     * @param charger - The charger that the user wishes to edit.
     * @param stage - The current stage.
     */
    private void loadEditCharger(Charger charger, Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader editChargerLoader = new FXMLLoader(getClass().getResource("/fxml/editCharger.fxml"));
            BorderPane editChargerParent = editChargerLoader.load();
            EditChargerController editChargerController = editChargerLoader.getController();
            editChargerController.init(charger, stage);
            mainWindow.setCenter(editChargerParent);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void loadViewCharger(Charger charger) {
        stage.setFullScreen(false);
        loadViewCharger(charger, stage);
    }

    /**
     * Injects map view into the current stage
     * @param stage The current stage
     */
    private void loadMapView(Stage stage, jsConnectorInitCaller caller) {
        stage.setFullScreen(false);
        try {
            FXMLLoader mapViewLoader = new FXMLLoader(getClass().getResource("/fxml/mapView.fxml"));
            Parent mapViewParent = mapViewLoader.load();
            MapController mapController = mapViewLoader.getController();
            mapController.init(stage, caller);
            mainWindow.setCenter(mapViewParent);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Injects table view into the current stage
     * @param stage The current stage
     */
    private void loadTableView(Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader tableViewLoader = new FXMLLoader(getClass().getResource("/fxml/tableView.fxml"));
            Parent tableViewParent = tableViewLoader.load();
            TableController tableController = tableViewLoader.getController();
            tableController.init(stage);
            mainWindow.setCenter(tableViewParent);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Injects register vehicle into the current stage
     * @param stage The current stage
     */
    private void loadRegisterVehicleView(Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader vehicleViewLoader = new FXMLLoader(getClass().getResource("/fxml/registerVehicle.fxml"));
            Parent vehicleViewParent = vehicleViewLoader.load();
            RegisterVehicleController vehicleController = vehicleViewLoader.getController();
            vehicleController.init(stage);
            mainWindow.setCenter(vehicleViewParent);
            stage.setHeight(650.0);
            stage.setWidth(900.0);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Injects general profile screen into the current stage
     * @param stage The current stage
     */
    private void loadGeneralProfileView(Stage stage) {
        stage.setFullScreen(false);

        if (UserManager.getIsUserLoggedIn()) {
            try {
                FXMLLoader settingsViewLoader = new FXMLLoader(getClass().getResource("/fxml/generalProfile.fxml"));
                Parent settingsViewParent = settingsViewLoader.load();
                GeneralProfileController settingsController = settingsViewLoader.getController();
                settingsController.init(stage);
                mainWindow.setCenter(settingsViewParent);
                stage.setHeight(650.0);
                stage.setWidth(900.0);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            Alert.notLoggedInAlert();
        }

    }

    /**
     * Injects help view into the current stage
     * @param stage The current stage
     */
    private void loadHelpView(Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader helpViewLoader = new FXMLLoader(getClass().getResource("/fxml/help.fxml"));
            Parent helpViewParent = helpViewLoader.load();
            HelpController helpController = helpViewLoader.getController();
            helpController.init(stage);
            mainWindow.setCenter(helpViewParent);
            stage.setHeight(650.0);
            stage.setWidth(900.0);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Injects the vehicle view into the current stage
     * @param stage the current stage
     */
    private void loadVehicleProfile(Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader vehiclesViewLoader = new FXMLLoader(getClass().getResource("/fxml/vehicles.fxml"));
            Parent vehicleViewParent = vehiclesViewLoader.load();
            VehicleController vehicleController = vehiclesViewLoader.getController();
            vehicleController.init(stage);
            mainWindow.setCenter(vehicleViewParent);
            stage.setHeight(650.0);
            stage.setWidth(900.0);
        } catch (IOException | NotLoggedInException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Opens login view in the current stage
     * @param stage The current stage
     */
    private void loadLogin(Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader loginViewLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent loginViewParent = loginViewLoader.load();
            LoginController loginController = loginViewLoader.getController();
            loginController.init(stage);
            Scene scene = new Scene(loginViewParent, 600, 400);
            stage.setScene(scene);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Injects the previous journeys screen into the current stage.
     * @param stage - The current stage
     */
    private void loadPreviousJourneys(Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader previousJourneysLoader = new FXMLLoader(getClass().getResource("/fxml/previousJourneys.fxml"));
            Parent previousJourneysParent = previousJourneysLoader.load();
            PreviousJourneysController previousJourneysController = previousJourneysLoader.getController();
            previousJourneysController.init(stage);
            mainWindow.setCenter(previousJourneysParent);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Injects the favourite chargers screen into the current stage.
     * @param stage - The current stage.
     */
    private void loadFavouriteChargers(Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader favouriteChargersLoader = new FXMLLoader(getClass().getResource("/fxml/favouriteChargers.fxml"));
            Parent favouriteChargersParent = favouriteChargersLoader.load();
            FavouriteChargersController favouriteChargersController = favouriteChargersLoader.getController();
            favouriteChargersController.init(stage);
            mainWindow.setCenter(favouriteChargersParent);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Injects the view profile screen into the current stage.
     * @param stage - The current stage.
     */
    private void loadViewProfile(Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader viewProfileLoader = new FXMLLoader(getClass().getResource("/fxml/viewProfile.fxml"));
            Parent viewProfileParent = viewProfileLoader.load();
            ViewProfileController viewProfileController = viewProfileLoader.getController();
            viewProfileController.init(stage);
            mainWindow.setCenter(viewProfileParent);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Injects the edit profile screen into the current stage.
     * @param stage - The current stage.
     */
    private void loadEditProfile(Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader editProfileLoader = new FXMLLoader(getClass().getResource("/fxml/editProfile.fxml"));
            Parent editProfileParent = editProfileLoader.load();
            EditProfileController editProfileController = editProfileLoader.getController();
            editProfileController.init(stage);
            mainWindow.setCenter(editProfileParent);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Inject the emergencies view into the current stage
     * @param stage The current stage
     */
    private void loadEmergenciesView(Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader emergenciesViewLoader = new FXMLLoader(getClass().getResource("/fxml/emergencies.fxml"));
            Parent emergenciesViewParent = emergenciesViewLoader.load();
            EmergenciesController emergenciesController = emergenciesViewLoader.getController();
            emergenciesController.init(stage);
            mainWindow.setCenter(emergenciesViewParent);
            stage.setHeight(650.0);
            stage.setWidth(900.0);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Injects the connector view into the current stage.
     * @param charger - The charger that the user wants to see.
     * @param stage - The current stage.
     */
    public void loadViewConnectors(Charger charger, Stage stage) {
        stage.setFullScreen(false);
        try {
            FXMLLoader connectorViewLoader = new FXMLLoader(getClass().getResource("/fxml/viewConnectors.fxml"));
            Parent connectorViewParent = connectorViewLoader.load();
            ViewConnectorsController viewConnectorsController = connectorViewLoader.getController();
            viewConnectorsController.init(charger, stage);
            mainWindow.setCenter(connectorViewParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls to load the create a connector view.
     * @param charger to add the connector to,
     */
    public void openCreateConnectorView(Charger charger, Boolean isNewCharger) {
        stage.setFullScreen(false);
        loadCreateConnectorView(charger, stage, isNewCharger);
        stage.setResizable(false);
        stage.setHeight(650.0);
        stage.setWidth(900.0);
    }

    /**
     * Injects the create a connector view into the current stage.
     * @param charger - The charger that the user wishes to add connectors to.
     * @param stage  - The current stage.
     */
    private void loadCreateConnectorView(Charger charger, Stage stage, Boolean isNewCharger) {
        stage.setFullScreen(false);
        try {
            FXMLLoader createConnectorViewLoader = new FXMLLoader(getClass().getResource("/fxml/createConnector.fxml"));
            Parent createConnectorViewParent = createConnectorViewLoader.load();
            CreateConnectorController createConnectorController = createConnectorViewLoader.getController();
            createConnectorController.init(stage, charger, isNewCharger);
            mainWindow.setCenter(createConnectorViewParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
