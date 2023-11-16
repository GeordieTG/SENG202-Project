package seng202.team5.views.windowControllers;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import seng202.team5.models.data.Charger;
import seng202.team5.models.data.Connector;
import seng202.team5.models.databaseInteraction.ChargerDAO;
import seng202.team5.views.SupplementaryGUIControllers.Alert;

/**
 * Controls the screen where users can edit a connector attached to a charger
 */
public class EditConnectorsController {

    /**
     * Text field to enter a new count
     */
    @FXML
    public TextField countText;
    /**
     * Text field to enter a new wattage
     */
    @FXML
    public TextField wattageText;
    /**
     * Dropdown to select a new power type
     */
    @FXML
    public ComboBox powerTypeDropdown;
    /**
     * Dropdown to select a new socket
     */
    @FXML
    public ComboBox socketDropdown;
    /**
     * Dropdown to select a new status
     */
    @FXML
    public ComboBox statusDropdown;
    /**
     * The connector being edited
     */
    private Connector connector;
    /**
     * The charger the connector is attached to
     */
    private Charger charger;
    /**
     * Used to update the charger after the connector has been edited/deleted
     */
    private final ChargerDAO chargerDAO = new ChargerDAO();


    /**
     * Connected to the back button. Takes the user back to the connector view screen
     */
    @FXML
    public void backToViewConnectors() {
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openViewConnectors(charger);
    }

    /**
     * Sets the dropdown options for the three dropdowns on this screen (power type, status, socket)
     */
    public void setDropdown() {

        // Socket Dropdown
        ObservableList<String> socket = FXCollections.observableArrayList("Type 1 Tethered", "Type 2 Socketed", "Type 2 Tethered", "Type 2 CCS", "CHAdeMO");
        socketDropdown.setValue(connector.getSocket());
        socketDropdown.setItems(socket);

        // Power Type Dropdown
        ObservableList<String> powerType = FXCollections.observableArrayList("AC", "DC");
        powerTypeDropdown.setValue(connector.getPowerType());
        powerTypeDropdown.setItems(powerType);

        // Status Dropdown
        ObservableList<String> status = FXCollections.observableArrayList("Operative", "Non-Operative");
        statusDropdown.setValue(connector.getStatus());
        statusDropdown.setItems(status);

    }

    /**
     * Initializes the stage
     * @param connector the connector being edited
     * @param charger the charger the connector is attached to
     */
    public void init(Connector connector, Charger charger) {
        /*
         * The current stage
         */
        this.connector = connector;
        this.charger = charger;

        setDropdown();

        // Set the text fields with connectors current information
        wattageText.setText(String.valueOf(connector.getWattage()));
        countText.setText(String.valueOf(connector.getCount()));
    }


    /**
     * Updates the display of a text field to have a red border to indicate that is invalid
     * @param textField the text field that is invalid
     */
    public void invalidTextField(TextField textField) {

        textField.setStyle("-fx-text-box-border: red;");

        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3)
        );
        visiblePause.setOnFinished(
                event -> textField.setStyle("")
        );
        visiblePause.play();
    }

    /**
     * Connected to the save button. Error checks to see if input is valid and saves the changes if so.
     */
    @FXML
    public void saveChanges() {

        // Check if text has been entered instead of number
        int error = 0;

        try {
            Integer.parseInt(countText.getText());
        } catch (Exception e) {
            invalidTextField(countText);
            error = 1;
        }
        try {
            Integer.parseInt(wattageText.getText());
        } catch (Exception e) {
            invalidTextField(wattageText);
            error = 1;
        }

        if (error == 1) {
            return;
        }

        // Check if count is invalid
        if (countText.getText().isBlank() || Integer.parseInt(countText.getText()) <= 0) {
            invalidTextField(countText);
        }

        // Check if wattage is invalid
        if (wattageText.getText().isBlank() || Integer.parseInt(wattageText.getText()) <= 0) {
            invalidTextField(wattageText);
        }

        // Update connector with passed in values
        if (!countText.getText().isBlank() && Integer.parseInt(countText.getText()) > 0
            && !wattageText.getText().isBlank() && Integer.parseInt(wattageText.getText()) > 0) {

            connector.setCount(Integer.parseInt(countText.getText()));
            connector.setWattage(Integer.parseInt(wattageText.getText()));
            connector.setSocket(String.valueOf(socketDropdown.getValue()));
            connector.setPowerType(String.valueOf(powerTypeDropdown.getValue()));
            connector.setStatus(String.valueOf(statusDropdown.getValue()));

            // Update connector in database
            chargerDAO.update(charger);
            backToViewConnectors();
        }
    }

    /**
     * Deletes the selected connector from the charger. Ensures that a charger must have at least one
     * connector at all times. Connected to the delete button
     */
    @FXML
    public void deleteConnector() {
        if (charger.getConnectorList().size() > 1) {
            charger.removeConnector(connector);
            chargerDAO.update(charger);
            backToViewConnectors();
        } else {
            Alert.minimumOneConnector();
        }
    }
}
