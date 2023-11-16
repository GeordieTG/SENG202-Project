package seng202.team5.views.windowControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team5.managers.UserManager;
import seng202.team5.models.data.User;
import seng202.team5.views.SupplementaryGUIControllers.Alert;

/**
 * Controller for generalProfile.fxml
 */
public class GeneralProfileController {

    /**
     * The stage for the settings view
     */
    Stage settingsStage = new Stage();


    /**
     * Calls the application logger
     */
    private static final Logger log = LogManager.getLogger();

    /**
     * Opens the view profile window when the button is pressed
     * @param event - profile button event
     */
    @FXML
    void openViewProfileWindow(ActionEvent event) {
        User user = null;
        try {
            user = UserManager.getCurrentlyLoggedIn();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (user != null) {
            MainController mainController = MainController.getMostRecentMainController();
            mainController.openViewProfile();
        } else {
            Alert.notLoggedInAlert();
        }
    }

    /**
     * Opens the vehicle(s) window when the button is pressed
     * @param event - the vehicle button event
     */
    @FXML
    void openVehiclesWindow(ActionEvent event) {

        User user = null;

        try {
            user = UserManager.getCurrentlyLoggedIn();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (user != null) {
            MainController mainController = MainController.getMostRecentMainController();
            mainController.openVehiclesWindow();
        } else {
            Alert.notLoggedInAlert();
        }
    }

    /**
     * Initialize the settings window
     * @param stage - settings controller stage
     */
    public void init(Stage stage) {
        settingsStage = stage;
    }

    /**
     * Opens the previous journey window.
     */
    public void openPreviousJourneysWindow() {
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openPreviousJourneysWindow();
    }

    /**
     * Opens the favourite charger window
     */
    public void openFavouriteChargersWindow() {
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openFavouriteChargers();
    }
}

