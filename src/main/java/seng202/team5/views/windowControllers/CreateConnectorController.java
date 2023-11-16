package seng202.team5.views.windowControllers;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import seng202.team5.models.data.Charger;
import seng202.team5.models.data.Connector;
import seng202.team5.models.databaseInteraction.ChargerDAO;

/**
 * Controls the screen where a user is able to create a new connector for the given charger
 */
public class CreateConnectorController {

    /**
     * Text field to enter a count
     */
    @FXML
    public TextField countText;
    /**
     * Text field to enter a wattage
     */
    @FXML
    public TextField wattageText;
    /**
     * Dropdown to select a power type
     */
    @FXML
    public ComboBox powerTypeDropdown;
    /**
     * Dropdown to select a socket
     */
    @FXML
    public ComboBox socketDropdown;
    /**
     * Dropdown to select a status
     */
    @FXML
    public ComboBox statusDropdown;
    /**
     * Displays when the user tries to create a charger without all the fields filled out
     */
    @FXML
    public Label errorLabel;
    /**
     * Takes the user back to the previous page
     */
    @FXML
    public Button backButton;
    /**
     * The charger the connector is being created for
     */
    private Charger charger;

    /**
     * Used to update the charger once the new connector has been created
     */
    private final ChargerDAO chargerDAO = new ChargerDAO();

    /**
     * This page is used coming off of various screens, meaning that the back button needs to lead to various places
     * This variable is used to differentiate these usages
     */
    private Boolean isNewCharger;

    /**
     * The current stage
     */
    private Stage stage;

    /**
     * Initializes the page
     * @param stage the current stage
     * @param charger the charger that the connector is being made for
     */

    public void init(Stage stage, Charger charger, Boolean isNewCharger) {
        this.stage = stage;
        this.charger = charger;
        this.isNewCharger = isNewCharger;
        if (isNewCharger) {
            backButton.setVisible(false);
        } else {
            backButton.setVisible(true);
        }
        setDropdown();
    }

    /**
     * Sets the dropdown options for the three dropdowns on the page (socket, status, power type)
     */
    public void setDropdown() {

        // Socket Dropdown
        ObservableList<String> socket = FXCollections.observableArrayList("Type 1 Tethered", "Type 2 Socketed", "Type 2 Tethered", "Type 2 CCS", "CHAdeMO");
        socketDropdown.setItems(socket);

        // Power Type Dropdown
        ObservableList<String> powerType = FXCollections.observableArrayList("AC", "DC");
        powerTypeDropdown.setItems(powerType);

        // Status Dropdown
        ObservableList<String> status = FXCollections.observableArrayList("Operative", "Non-Operative");
        statusDropdown.setItems(status);

    }

    /**
     * Connected to the back button. Takes the users back to the connector view screen
     */
    @FXML
    public void backToViewConnectors() {
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openViewConnectors(charger);
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
     * Creates the connector from the user filled fields. Error checks to make sure that all fields are legitimate
     * before creation. Connected to the save button. Returns 1 if successful, 0 if not
     */
    @FXML
    public int createConnector() {

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
            return 0;
        }

        // Check if count is invalid
        if (countText.getText().isBlank() || Integer.parseInt(countText.getText()) <= 0) {
            invalidTextField(countText);
        }

        // Check if wattage is invalid
        if (wattageText.getText().isBlank() || Integer.parseInt(wattageText.getText()) <= 0) {
            invalidTextField(wattageText);
        }

        // Create if input is valid
        if (!countText.getText().isBlank() && Integer.parseInt(countText.getText()) > 0
                && !wattageText.getText().isBlank() && Integer.parseInt(wattageText.getText()) > 0
                && statusDropdown.getValue() != null && powerTypeDropdown.getValue() != null
                && socketDropdown.getValue() != null) {

            // Update connector in database
            Connector connector = new Connector((String) powerTypeDropdown.getValue(), (String) socketDropdown.getValue(), Integer.parseInt(wattageText.getText()), (String) statusDropdown.getValue(), Integer.parseInt(countText.getText()));
            charger.addConnector(connector);
            chargerDAO.update(charger);
            backToViewConnectors();
            return 1;

        } else {
            errorLabel.setVisible(true);

            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3)
            );
            visiblePause.setOnFinished(
                    event -> errorLabel.setVisible(false)

            );
            visiblePause.play();
            return 0;
        }
    }

    /**
     * Checks if the connector that the user has created is valid, if it is then will open another one.
     */
    public void addAnother() {
        if (createConnector() == 1) {
            MainController mainController = MainController.getMostRecentMainController();
            mainController.openCreateConnectorView(charger, isNewCharger);
        }
    }
}
