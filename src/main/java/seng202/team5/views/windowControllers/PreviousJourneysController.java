package seng202.team5.views.windowControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import seng202.team5.exceptions.NotLoggedInException;
import seng202.team5.managers.TableManager;
import seng202.team5.managers.UserManager;
import seng202.team5.models.data.Journey;
import seng202.team5.models.data.User;
import seng202.team5.views.SupplementaryGUIControllers.Alert;
import seng202.team5.views.SupplementaryGUIControllers.jsConnectorInitCaller;
import java.util.ArrayList;

/**
 * The controller class for user's viewing their previous journeys
 */
public class PreviousJourneysController extends jsConnectorInitCaller {

    /**
     * The stage of the previous journeys
     */
    Stage previousJourneysStage = new Stage();

    /**
     * The current journeys of the user
     */
    ArrayList<Journey> currentJourneys;

    /**
     * The table to view previous journeys
     */
    @FXML
    TableView<Journey> journeyTableView;

    /**
     * Button for putting the journey back on map
     */
    @FXML
    Button journeyOnMapButton;

    /**
     * Label for error messages
     */
    @FXML
    Label labelErrorMessage;

    /**
     * The button for deleting journeys
     */
    @FXML
    Button deleteButton;

    /**
     * Initializes the previous journey screen and populates table
     * @param stage The current stage
     */
    public void init(Stage stage) throws NotLoggedInException {
        previousJourneysStage = stage;
        currentJourneys = getUserJourneys();
        fillTable(currentJourneys);
    }

    /**
     * Fills the table with journeys from user
     * @param journey The current journeys of the user
     */
    public void fillTable(ArrayList<Journey> journey) {
        TableManager.initJourneyTable(journeyTableView, journey);
    }

    /**
     * Gets the journeys that are currently stored in user.
     * @return ArrayList of journeys that the user has.
     * @throws NotLoggedInException if the user is not logged in.
     */
    public ArrayList<Journey> getUserJourneys() throws NotLoggedInException {
        User currUser = UserManager.getCurrentlyLoggedIn();

        return currUser.getSavedJourneyList();
    }

    /**
     * Shows the journey that is currently selected in the table
     */
    public void showSelectedJourneyOnMap () {
        Journey selectedJourney = journeyTableView.getSelectionModel().getSelectedItem();
        if (selectedJourney == null) {
            labelErrorMessage.setText("No Journey Selected");
            labelErrorMessage.setVisible(true);
            return;
        }
        addMapInitCommand("addRoute");
        addMapInitParams(new Object[]{selectedJourney.getStops()});
        Journey.setCurrentJourneySingleton(selectedJourney);
        startMap();
    }

    /**
     * Deletes a journey from the user when delete button is pressed
     */
    public void deleteJourney() {

        Journey selectedJourney = journeyTableView.getSelectionModel().getSelectedItem();
        if (selectedJourney == null) {
            labelErrorMessage.setText("No Journey Selected");
            labelErrorMessage.setVisible(true);
        } else {

            try {
                journeyTableView.getItems().remove(selectedJourney);

                UserManager current = new UserManager();
                User updatedUser = UserManager.getCurrentlyLoggedIn();
                current.update(updatedUser);

                labelErrorMessage.setText("Journey Deleted");
                labelErrorMessage.setVisible(true);
            } catch (Exception e) {
                Alert.notLoggedInAlert();
            }
        }

    }

}
