package seng202.team5.views.windowControllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Duration;
import seng202.team5.exceptions.NotFoundException;
import seng202.team5.exceptions.NotLoggedInException;
import seng202.team5.managers.TableManager;
import seng202.team5.managers.UserManager;
import seng202.team5.models.data.Charger;
import seng202.team5.views.SupplementaryGUIControllers.jsConnectorInitCaller;
import java.sql.SQLException;

/**
 * Controller for the window responsible for display the users favourite chargers
 */

public class FavouriteChargersController extends jsConnectorInitCaller {

    /**
     * Error message displayed when no charger is selected
     */
    public Label errorLabel;
    /**
     * Current stage
     */
    Stage favouriteChargersStage;
    /**
     * The favourite chargers table
     */
    @FXML
    TableView<Charger> chargerTableView;

    /**
     * Initialises the page
     * @param stage The stage that is being displayed
     */
    public void init(Stage stage) throws NotLoggedInException, SQLException, NotFoundException {
        favouriteChargersStage = stage;
        initChargerTable();
    }

    /**
     * Generates and populates the charger table
     */
    private void initChargerTable() throws NotLoggedInException, SQLException, NotFoundException {
        TableManager.initChargerTable(chargerTableView, UserManager.getCurrentlyLoggedIn().favouriteChargersToObjects());
    }

    /**
     * Removes the selected charger from the users favourite charger list and updates the database/table
     * Displays a timed error message if no charger is selected
     */
    public void removeChargerFromFavourites() {
        try {
            Charger selectedCharger = chargerTableView.getSelectionModel().getSelectedItem();
            UserManager.getCurrentlyLoggedIn().removeChargerFromFavourites(selectedCharger);
            init(favouriteChargersStage);
        } catch (Exception e) {
            noChargerSelected();
        }
    }

    /**
     * Allows the user to go back to the settings page when back has been pressed.
     */
    @FXML
    public void backToSettings() {
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openSettingsWindow();
    }

    /**
     * Shows error label when no charger is selected
     */
    public void noChargerSelected() {
        // Shows the error label for three seconds and then becomes invisible again
        errorLabel.setVisible(true);

        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3)
        );
        visiblePause.setOnFinished(
                event -> errorLabel.setVisible(false)
        );
        visiblePause.play();
    }


    /**
     * View selected charger on map
     */
    @FXML
    public void viewOnMap() {

        Charger selectedCharger = chargerTableView.getSelectionModel().getSelectedItem();

        if (selectedCharger != null) {

            addMapInitCommand("viewChargerOnMap");
            Object[] objects = new Object[]{selectedCharger.getLocation().getLatitude(), selectedCharger.getLocation().getLongitude()};
            addMapInitParams(objects);
            startMap();
        } else {
            noChargerSelected();
        }

    }
}
