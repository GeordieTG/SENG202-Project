package seng202.team5.views.windowControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team5.managers.ChargerManager;
import seng202.team5.models.data.Charger;
import seng202.team5.models.data.Location;
import java.io.IOException;

/**
 * Controller for the editCharger.fxml window
 */
public class EditChargerController {

    /**
     * Textfield to change Charger Name
     */
    @FXML
    private TextField chargerName;

    /**
     * Textfield to change Charger Latitude
     */
    @FXML
    private TextField chargerLatitude;

    /**
     * Textfield to change Charger Longitude
     */
    @FXML
    private TextField chargerLongitude;

    /**
     * Textfield to change Charger Operator
     */
    @FXML
    private TextField chargerOperator;

    /**
     * Textfield to change Charger Owner
     */
    @FXML
    private TextField chargerOwner;

    /**
     * Textfield to change Charger Address
     */
    @FXML
    private TextField chargerAddress;

    /**
     * Textfield to change Charger Max Use Time
     */
    @FXML
    private TextField chargerMaxTime;

    /**
     * Textfield for charger carpark count
     */
    @FXML
    TextField carparkCountTextField;

    /**
     * Textfield for charger rating
     */
    @FXML
    private TextField chargerRating;

    /**
     * Label to display text upon saved
     */
    @FXML
    private Label labelSaved;

    @FXML
    private Button editCharger;

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
     * The charger that is being edited
     */
    private Charger toEdit;


    /**
     * Calls the application logger
     */
    private static final Logger log = LogManager.getLogger();


    /**
     * Method to save changes made to charger by the user
     * Only changes if associated textField has been filled
     */
    public void save() {
        Charger charger = toEdit;
        ChargerManager chargerManager = new ChargerManager();

        if (highlightErrors()) {
            return;
        }

        try {
            chargerManager.delete(charger);
            charger.setName(chargerName.getText());
            Location newLocation = new Location(Float.parseFloat(chargerLatitude.getText()), Float.parseFloat(chargerLongitude.getText()));
            charger.setLocation(newLocation);
            charger.setOperator(chargerOperator.getText());
            charger.setOwner(chargerOwner.getText());
            charger.setMaxTimeLimit(Integer.parseInt(chargerMaxTime.getText()));
            charger.setAddress(chargerAddress.getText());
            charger.setAlwaysOpen(alwaysOpenCheck.isSelected());
            charger.setCarparkCount(Integer.parseInt(carparkCountTextField.getText()));
            charger.setCarparkNeedsPayment(parkPaymentCheck.isSelected());
            charger.setTouristAttraction(touristAttractCheck.isSelected());
            charger.setRating(Integer.parseInt(chargerRating.getText()));
            chargerManager.addCharger(charger);

            back();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    /**
     * Checks the text given is valid
     * @param textField Textfield to be checked
     * @return Boolean, false if text is fine, true is text field is empty
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
     * Follows implementation from advanced-FX-lab by Morgan English
     * @return true if there is an issue with the input fields
     */
    private boolean highlightErrors() {
        boolean a = checkTextField(chargerName);
        boolean b = checkTextField(chargerAddress);
        boolean c = checkFloatField(chargerLatitude, -90, 90);
        boolean d = checkFloatField(chargerLongitude, -180, 180);

        boolean e = checkTextField(chargerOperator);
        boolean f = checkTextField(chargerOwner);
        boolean g = checkIntField(carparkCountTextField, 0, 100);
        boolean h = checkIntField(chargerMaxTime, 0, 2147483647);
        boolean i = checkIntField(chargerRating, 0, 5);
        return a || b || c || d || e || f || g || h || i;
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
     * @param min Minimum value
     * @param max Maximum value
     * @return Boolean describing if float is valid
     */
    private boolean checkIntField(TextField text, int min, int max) {
        boolean good = !checkTextField(text);
        try {
            int tempInt = Integer.parseInt(text.getText());
            if (tempInt < min || tempInt > max) {
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
     * Method called upon when the screen is launched which prefills the textfields with the current data of the charger
     * @param charger The current charger that was selected to be edited
     */
    public void showData (Charger charger) {
        chargerName.setText(charger.getName());
        chargerLatitude.setText(String.valueOf(charger.getLatitude()));
        chargerLongitude.setText(String.valueOf(charger.getLongitude()));
        chargerOperator.setText(charger.getOperator());
        chargerOwner.setText(charger.getOwner());
        chargerAddress.setText(charger.getAddress());
        chargerMaxTime.setText(String.valueOf(charger.getMaxTimeLimit()));
        alwaysOpenCheck.setSelected(charger.isAlwaysOpen());
        carparkCountTextField.setText(String.valueOf(charger.getCarParkCount()));
        parkPaymentCheck.setSelected(charger.isCarParkNeedsPayment());
        touristAttractCheck.setSelected(charger.isTouristAttraction());
        chargerRating.setText(String.valueOf(charger.getRating()));

    }

    /**
     * Method called upon when the back button is selected
     * Closes the edit charger screen and opens the view charger screen
     * @throws IOException if the window cannot be loaded.
     */
    public void back() throws IOException {
        MainController.getMostRecentMainController().openViewCharger(toEdit);
    }


    /**
     * Initialises the page
     * @param toEdit The charger that is selected to be edited
     */
    public void init(Charger toEdit, Stage stage) {
        /*
         * JavaFX stage for the view charger
         */
        this.toEdit = toEdit;
        showData(this.toEdit);
    }

}
