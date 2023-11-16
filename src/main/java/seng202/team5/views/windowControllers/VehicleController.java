package seng202.team5.views.windowControllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seng202.team5.exceptions.NotLoggedInException;
import seng202.team5.managers.UserManager;
import seng202.team5.models.data.User;
import seng202.team5.models.data.Vehicle;
import seng202.team5.views.SupplementaryGUIControllers.Alert;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller for the vehicles.fxml
 */
public class VehicleController {

    /**
     * Sets the stage
     */
    Stage vehiclesStage = new Stage();

    /**
     * Table that will show vehicles
     */
    @FXML
    private TableView<Vehicle> vehicleTableView;

    /**
     * The error label
     */
    @FXML
    private Label errorLabel;



    /**
     * The selected vehicle on the table
     */
    private Vehicle selectedVehicle;



    /**
     * Opens the settings window
     * @throws IOException Exception is thrown when view cannot be opened
     */
    @FXML
    void openSettingsWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.init(vehiclesStage, 2);

        vehiclesStage.setTitle("EVOH");
        vehiclesStage.setScene(new Scene(root));
        vehiclesStage.show();
    }

    /**
     * Gets the vehicle(s) of current logged-in User
     * @return The ArrayList of vehicles from user
     * @throws NotLoggedInException Thrown when the user is not logged in
     */
    public ArrayList<Vehicle> getVehicle() throws NotLoggedInException {

        User currentUser = UserManager.getCurrentlyLoggedIn();

        return currentUser.getVehicleList();

    }

    /**
     * Initializes the vehicle table with the user's vehicles
     * @throws NotLoggedInException Exception is thrown when user is not logged in
     */
    public void initVehicleTable() throws NotLoggedInException {

        TableColumn<Vehicle, String> plateCol = new TableColumn<>("Plate No.");
        plateCol.setCellValueFactory(new PropertyValueFactory<>("plateNumber"));

        TableColumn<Vehicle, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Vehicle, String> fuelTypeCol = new TableColumn<>("Fuel Type");
        fuelTypeCol.setCellValueFactory(new PropertyValueFactory<>("fuelType"));

        TableColumn<Vehicle, String> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Vehicle, String> rangeCol = new TableColumn<>("Range");
        rangeCol.setCellValueFactory(new PropertyValueFactory<>("range"));

        vehicleTableView.getColumns().add(plateCol);
        vehicleTableView.getColumns().add(modelCol);
        vehicleTableView.getColumns().add(fuelTypeCol);
        vehicleTableView.getColumns().add(yearCol);
        vehicleTableView.getColumns().add(rangeCol);

        vehicleTableView.setItems(FXCollections.observableList(getVehicle()));
    }

    /**
     * Deletes a selected vehicle from the table view.
     */
    @FXML
    public void deleteVehicle() throws NotLoggedInException {

        try{
            selectedVehicle = vehicleTableView.getSelectionModel().getSelectedItem();
        } catch (NullPointerException nullPointerException) {
            errorLabel.setText("No Vehicle Selected");
            errorLabel.setVisible(true);
        }

        if (selectedVehicle != null) {
            if (Alert.deleteConfirmation()) {
                vehicleTableView.getItems().remove(selectedVehicle);
                UserManager current = new UserManager();
                User updatedUser = UserManager.getCurrentlyLoggedIn();
                current.update(updatedUser);

                errorLabel.setText("Vehicle Deleted");
                errorLabel.setVisible(true);
            }


        } else {

            errorLabel.setText("No Vehicle Selected");
            errorLabel.setVisible(true);

        }
    }

    /**
     * Initializes the stage and table
     * @param stage The current stage
     * @throws NotLoggedInException Exception thrown when user is not logged in
     */
    public void init(Stage stage) throws NotLoggedInException {
        vehiclesStage = stage;
        initVehicleTable();
    }

}
