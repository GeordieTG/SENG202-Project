package seng202.team5.views.windowControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team5.managers.UserManager;
import seng202.team5.models.data.User;
import seng202.team5.models.data.Vehicle;
import java.io.IOException;

/**
 * Controller for registerVehicle fxml
 */
public class RegisterVehicleController {

    /**
     * Sets up the stage
     */
    Stage registerVehicleStage = new Stage();

    /**
     * Text field for the vehicle model
     */
    @FXML
    private TextField ModelText;

    /**
     * The Text field for the vehicle year
     */
    @FXML
    private TextField YearText;

    /**
     * The Text field for the vehicle plate
     */
    @FXML
    private TextField PlateText;

    /**
     * Error label
     */
    @FXML
    private Label testLabel;

    /**
     * The Text field for the vehicle range
     */
    @FXML
    private TextField RangeText;

    /**
     * Drop down box for fuel type
     */
    @FXML
    private ComboBox fuelTypeDropDown;

    /**
     * Calls the application logger
     */
    private static final Logger log = LogManager.getLogger();

    /**
     * Ran when the complete button is pressed
     */
    public void complete() throws IOException {

        String FuelText = (String) fuelTypeDropDown.getValue();

        // If any of the text fields are empty
        if (ModelText.getText().isEmpty() || YearText.getText().isEmpty() || PlateText.getText().isEmpty()
                || FuelText.equals("Select Fuel Type") || RangeText.getText().isEmpty()) {
            testLabel.setText("Please Input Vehicle Data");
        } else {
            int year;
            int range;

            try {
                year = Integer.parseInt(YearText.getText());
            } catch (Exception e) {
                testLabel.setText("Invalid Year!");
                testLabel.setVisible(true);
                return;
            }
            if (year > 2024) {
                testLabel.setText("Invalid Year!");
                testLabel.setVisible(true);
                return;
            }
            try {
                range = Integer.parseInt(RangeText.getText());
            } catch (Exception e) {
                testLabel.setText("Invalid Range!");
                testLabel.setVisible(true);
                return;
            }
            if (range > 836) {
                testLabel.setText("Invalid Range!");
                testLabel.setVisible(true);
                return;
            }
            /// Create the vehicle
            Vehicle newVehicle = new Vehicle(PlateText.getText(), ModelText.getText(), FuelText,
                    Integer.parseInt(YearText.getText()), range);

            UserManager uManager = new UserManager();

            User user = null;
            try {
                user = UserManager.getCurrentlyLoggedIn();
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            // Add vehicle to user list
            if (user != null) {
                user.addVehicle(newVehicle);
                uManager.update(user);
            }
            openMain();
        }
    }

    /**
     * Method to call when mapview application window needs to be opened (back is pressed)
     */
    public void openMain() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.init(registerVehicleStage, 0);

        registerVehicleStage.setTitle("EVOH");
        registerVehicleStage.setScene(new Scene(root));
        registerVehicleStage.show();
    }

    /**
     * Sets the ComboBoxes (Drop Downs) in the GUI
     */
    public void setDropDown() {

        ObservableList<String> fuelTypes = FXCollections.observableArrayList("Electric", "Hybrid", "Petrol", "Diesel");
        fuelTypeDropDown.setValue("Select Fuel Type");
        fuelTypeDropDown.setItems(fuelTypes);
    }

    /**
     * Initializes the window
     * Code adapted from the Advanced JavaFX lab by Morgan.
     * @param stage Top level container for this window
     */
    public void init(Stage stage) {
        registerVehicleStage = stage;
        setDropDown();

        fuelTypeDropDown.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER ) {
                try {
                    complete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
