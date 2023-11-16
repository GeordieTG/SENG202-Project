package seng202.team5.views.windowControllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng202.team5.models.data.Charger;
import seng202.team5.models.data.Connector;

/**
 * Handles the functionality of the Connector View Screen
 */

public class ViewConnectorsController {

    /**
     * Displays the power type of the current connector
     */
    @FXML
    public Text powerTypeText;

    /**
     * Displays the socket type of the current connector
     */
    @FXML
    public Text socketText;

    /**
     * Displays the status of the current connector
     */
    @FXML
    public Text statusText;
    /**
     * Displays the wattage of the current connector
     */
    @FXML
    public Text wattageText;
    /**
     * Displays the count of unique connectors (maximum page number)
     */
    @FXML
    public Text countText;

    /**
     * Displays what number connector is being viewed (current page number)
     */
    @FXML
    public Text currentConnectorText;

    /**
     * Displays the amount of connectors that meet this connector description
     * (identical connectors are displayed as one)
     */
    @FXML
    public Text identicalCountText;
    Stage stage;

    /**
     * The charger in which the connectors belong to
     */
    Charger viewedCharger;

    /**
     * The current connector being displayed
     */
    int currentConnector = 1;

    /**
     * Initializes the screen
     * @param toView the Charger in which the connectors belong to
     * @param stage the current stage
     */
    public void init(Charger toView, Stage stage) {
        this.stage = stage;
        this.viewedCharger = toView;
        countText.setText(" / " + viewedCharger.getConnectorList().size());


        // Get first connector from charger and display it
        displayConnector();
    }

    /**
     * Takes the user back to the Charger View
     */
    @FXML
    public void backToViewCharger() {
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openViewCharger(viewedCharger);
    }

    /**
     * Displays the current connector's information
     * All identical connectors are group together and displayed with a count of how
     * many fit this description
     */
    public void displayConnector() {
        Connector connector = viewedCharger.getConnectorList().get(currentConnector - 1);
        currentConnectorText.setText(String.valueOf(currentConnector));
        powerTypeText.setText(connector.getPowerType());
        socketText.setText(connector.getSocket());
        statusText.setText(connector.getStatus());
        wattageText.setText(connector.getWattage() + " kW");
        identicalCountText.setText(String.valueOf(connector.getCount()));
    }

    /**
     * Displays the next connector type of this charger. If the user is viewing the last connector it
     * will loop back to display the first connector
     */
    @FXML
    public void displayNextConnector() {
        if (currentConnector < viewedCharger.getConnectorList().size()) {
            currentConnector += 1;
            displayConnector();
        } else {
            currentConnector = 1;
            displayConnector();
        }
    }

    /**
     * Connected to the EDIT button. Takes the user to a page where they are prompted to edit
     * the current connector
     */
    @FXML
    public void openEditConnectorView() {
        Connector connector = viewedCharger.getConnectorList().get(currentConnector - 1);
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openEditConnectors(connector, viewedCharger);
    }

    /**
     * Displays the previous connector type of this charger. If the user is viewing the first
     * connector than it loops back to display the last connector
     */
    @FXML
    public void displayPreviousConnector() {

        if (currentConnector > 1) {
            currentConnector -= 1;
            displayConnector();
        } else {
            currentConnector = viewedCharger.getConnectorList().size();
            displayConnector();
        }

    }

    /**
     * Connected to the NEW button. Takes the user to a page where they are prompted to create a new connector
     * for the given charger.
     */
    @FXML
    public void newConnector() {
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openCreateConnectorView(viewedCharger, false);
    }
}
