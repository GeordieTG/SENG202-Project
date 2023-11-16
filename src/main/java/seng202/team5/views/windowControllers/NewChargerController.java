package seng202.team5.views.windowControllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;
import seng202.team5.exceptions.NotFoundException;
import seng202.team5.managers.ChargerManager;
import seng202.team5.models.data.Charger;
import seng202.team5.managers.Geolocation;
import seng202.team5.models.data.Location;
import seng202.team5.models.databaseInteraction.ChargerDAO;
import seng202.team5.views.SupplementaryGUIControllers.Alert;

import java.sql.SQLException;


/**
 * Controller for the newCharger.fxml window
 */
public class NewChargerController {

    private final Geolocation geolocator = new Geolocation();

    private Stage stage;

    /**
     * Textfield for charger name
     */
    @FXML
    TextField nameTextField;

    /**
     * Textfield for charger latitude
     */
    @FXML
    TextField latitudeTextField;

    /**
     * Textfield for charger longitude
     */
    @FXML
    TextField longitudeTextField;

    /**
     * Textfield for charger Operator
     */
    @FXML
    TextField operatorTextField;

    /**
     * Textfield for charger Owner
     */
    @FXML
    TextField ownerTextField;

    /**
     * Textfield for charger address
     */
    @FXML
    TextField addressTextField;

    /**
     * Textfield for charger carpark count
     */
    @FXML
    TextField carparkCountTextField;

    /**
     * Textfield for charger max time
     */
    @FXML
    TextField maxTimeTextField;

    /**
     * Checkbox for charger always open bool
     */
    @FXML
    CheckBox alwaysOpenCheck;

    /**
     * Checkbox for charger park payment bool
     */
    @FXML
    CheckBox parkPaymentCheck;

    /**
     * Checkbox for charger tourist attraction bool
     */
    @FXML
    CheckBox touristAttractCheck;

    /**
     * Button to add charger
     */
    @FXML
    Button addChargerButton;

    /**
     * Button to validate address
     */
    @FXML
    Button validateButton;

    private ChargerDAO chargerDAO = new ChargerDAO();

    /**
     * Adds given charger to the dataset
     * @throws InstantiationError Exception thrown when new instance cannot be created
     * @throws IllegalAccessError Exception thrown when variable cannot be modified
     * @throws NoSuchMethodError Exception thrown when invalid method is called
     */
    @FXML
    void addCharger() throws InstantiationError, IllegalAccessError, NoSuchMethodError {
        if (highlightErrors()) {
            return;
        }

        int success;

        try {
            chargerDAO.getFromLatLong(Float.parseFloat(latitudeTextField.getText()), Float.parseFloat(longitudeTextField.getText()));
            // There is a charger at this location already
            success = 0;
        } catch (Exception e) {
            // There is no charger at this location already
            success = 1;
        }

        if (success == 1) {
            Charger newCharger = new Charger();
            newCharger.setLocation(new Location(Float.parseFloat(latitudeTextField.getText()), Float.parseFloat(longitudeTextField.getText())));
            String address = geolocator.getAddressFromLocation(Float.parseFloat(latitudeTextField.getText()), Float.parseFloat(longitudeTextField.getText()));
            newCharger.setName(nameTextField.getText());
            newCharger.setOperator(operatorTextField.getText());
            newCharger.setOwner(ownerTextField.getText());
            newCharger.setAddress(address);
            newCharger.setAlwaysOpen(alwaysOpenCheck.isSelected());
            newCharger.setCarparkCount(Integer.parseInt(carparkCountTextField.getText()));
            newCharger.setCarparkNeedsPayment(parkPaymentCheck.isSelected());
            newCharger.setMaxTimeLimit(Integer.parseInt(maxTimeTextField.getText()));
            newCharger.setTouristAttraction(touristAttractCheck.isSelected());
            ChargerManager chargerManager = new ChargerManager();
            chargerManager.addCharger(newCharger);

            MainController mainController = MainController.getMostRecentMainController();
            mainController.openCreateConnectorView(newCharger, true);

        } else {
            Alert.invalidChargerLocation();
        }
    }

    /**
     * Copied from advanced-fx-lab written by morgan english
     * Checks if an object of type TextInputControl is empty
     * @param textField text field (or other implementation of TextInputControl) to check
     * @return true if empty, false otherwise
     */
    private boolean checkTextField(TextInputControl textField){
        if(textField.getText().isEmpty() || textField.getText().isBlank()) {
            textField.setStyle("-fx-text-box-border: red;");
            return true;
        }
        textField.setStyle("");
        return false;
    }


    /**
     * Checks the float is a float and between min and max values
     * @param text Given text field to check
     * @param min Minimum value
     * @param max Maximum value
     * @return Boolean describing if float is valid
     */
    private boolean checkFloatField(TextField text, float min, float max) {
        boolean good = !checkTextField(text);
        try {
            float tempFloat = Float.parseFloat(text.getText());
            if (tempFloat < min || tempFloat > max) {
                good = false;
            }
        } catch (Exception ignored) {
            good = false;
        }

        if (good) {
            text.setStyle("");
            return false;
        } else {
            text.setStyle("-fx-text-box-border: red;");
            return true;
        }
    }

    /**
     * Checks the integer is an integer and between min and max values
     * @param text Given text field to check
     * @param max Maximum value
     * @return Boolean describing if float is valid
     */
    private boolean checkIntField(TextField text, int max) {
        boolean good = !checkTextField(text);
        try {
            int tempInt = Integer.parseInt(text.getText());
            if (tempInt < 0 || tempInt > max) {
                good = false;
            }
        } catch (Exception ignored) {
            good = false;
        }

        if (good) {
            text.setStyle("");
            return false;
        } else {
            text.setStyle("-fx-text-box-border: red;");
            return true;
        }
    }

    /**
     * Follows implementation from advanced-FX-lab by Morgan English
     * @return true if there is an issue with the input fields
     */
    private boolean highlightErrors() {
        boolean a = checkTextField(nameTextField);
        boolean c = checkFloatField(latitudeTextField, -90, 90);
        boolean d = checkFloatField(longitudeTextField, -180, 180);
        boolean e = checkTextField(operatorTextField);
        boolean f = checkTextField(ownerTextField);
        boolean g = checkIntField(carparkCountTextField, 100);
        boolean h = checkIntField(maxTimeTextField, 2147483647);
        return a || c || d || e || f || g || h;
    }

    /**
     * Sets the address boxes to red amd clears latitude and longitude boxes if the address entered was invalid
     */
    public void invalidAddress() {
        latitudeTextField.setText("");
        longitudeTextField.setText("");

        addressTextField.setStyle("-fx-text-box-border: red;");

        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3)
        );
        visiblePause.setOnFinished(
                event -> addressTextField.setStyle("")
        );
        visiblePause.play();
    }
    /**
     * Uses the Geolocation class to take the entered address and autofill latitude and longitude
     */
    public void validateAddress() {
        if (!addressTextField.getText().isBlank()) {
            Location location  = geolocator.queryAddress(addressTextField.getText());
            if(location.getLatitude() == 0 & location.getLongitude() == 0) {
                invalidAddress();
            } else {
                latitudeTextField.setText(Float.toString(location.getLatitude()));
                longitudeTextField.setText(Float.toString(location.getLongitude()));
            }
        } else {
            invalidAddress();
        }
    }

    /**
     * Initialises the page
     */
    public void init (Stage stage) {

        this.stage = stage;

        longitudeTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER ) {
                addCharger();
            }
        });
    }

    /**
     * Connected to the back button. Takes the user back to the table view
     */
    @FXML
    public void backToTableView() {
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openTableWindow();
    }
}
