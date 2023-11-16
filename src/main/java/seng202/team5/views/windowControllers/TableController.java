package seng202.team5.views.windowControllers;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang3.NotImplementedException;
import seng202.team5.exceptions.NotEnteredException;
import seng202.team5.exceptions.NotFoundException;
import seng202.team5.exceptions.NotLoggedInException;
import seng202.team5.managers.ChargerManager;
import seng202.team5.managers.FilterManager;
import seng202.team5.managers.TableManager;
import seng202.team5.managers.UserManager;
import seng202.team5.models.csvInteraction.ChargerCSVReader;
import seng202.team5.models.csvInteraction.ChargerCSVWriter;
import seng202.team5.models.data.Charger;
import seng202.team5.models.data.User;
import seng202.team5.models.databaseInteraction.Filter;
import seng202.team5.models.data.Location;
import seng202.team5.views.SupplementaryGUIControllers.Alert;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Controller for the tableView.fxml window
 */
public class TableController {

    /**
     * Slider for carparks at charger
     */
    @FXML
    public Slider numberOfParksSlider;
    /**
     * Slider for max time
     */
    @FXML
    public Slider maxTimeLimitSlider;
    /**
     * Button to save charger to favourites
     */
    @FXML
    public Button favouriteButton;
    /**
     * Label to add charger to favourites
     */
    @FXML
    public Label addToFavouritesLabel;
    /**
     * The stage for the tableView
     */
    Stage tableStage;

    /**
     * Instance of filter manager to allow filtering of data
     */
    final FilterManager filterManager = new FilterManager();

    /**
     * The ChargerManager for the application
     */
    private final ChargerManager chargerManager = new ChargerManager();

    /**
     * The Table that shows the chargers
     */
    @FXML
    private TableView<Charger> chargerTableView;

    /**
     * Checkbox for alwaysOpen filter
     */
    @FXML
    private CheckBox alwaysOpenCheck;

    /**
     * Checkbox for carParkNeedsPayment filter
     */
    @FXML
    private CheckBox carParkNeedsPaymentCheck;

    /**
     * Checkbox for touristAttraction filter
     */
    @FXML
    private CheckBox touristAttractionCheck;

    /**
     * Error message label
     */
    @FXML
    private Label labelErrorMessage;

    /**
     * Checkbox for type 1 tethered connector
     */
    @FXML
    public CheckBox type1TetheredCheckBox;
    /**
     * Checkbox for type 2 socket connector
     */
    @FXML
    public CheckBox type2SocketedCheckBox;
    /**
     * Checkbox for type 2 ccs connector
     */
    @FXML
    public CheckBox type2CCSCheckBox;
    /**
     * Checkbox for CHAdeMO connector
     */
    @FXML
    public CheckBox CHAdeMOCheckBox;
    /**
     * Checkbox for type 2 tethered connector
     */
    @FXML
    public CheckBox type2TetheredCheckBox;

    /**
     * Charger selected by the user
     */
    private Charger selectedCharger = null;

    /**
     *Method interacts with the filter manager and then calls the filterTable method
     */
    @FXML
    void filter() {
        ArrayList<Filter> filters = filterManager.filter(alwaysOpenCheck, carParkNeedsPaymentCheck, touristAttractionCheck,numberOfParksSlider,maxTimeLimitSlider, type1TetheredCheckBox, type2SocketedCheckBox, type2CCSCheckBox, type2TetheredCheckBox, CHAdeMOCheckBox);
        filterTable(filters);
    }

    /**
     * Adapted from advanced FX lab
     * Opens the directory browser to allow the user to load data from a CSV into the database
     */
    @FXML
    void loadDataFromFile() {
        File file;
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Charger CSV File");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            file = fileChooser.showOpenDialog(tableStage);
            fileChooser.setInitialDirectory(new File(MainController.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile());
        } catch (URISyntaxException e) {
            seng202.team5.views.SupplementaryGUIControllers.Alert.failedToLoadFile();
            return;
        }
        String fileString;
        try {
            fileString = file.getAbsolutePath();
        } catch (Exception ignored) {
            seng202.team5.views.SupplementaryGUIControllers.Alert.failedToLoadFile();
            return;
        }

        ChargerManager chargerManager = new ChargerManager();
        ChargerCSVReader csvReader = new ChargerCSVReader();

        try {
            chargerManager.addBatch(csvReader.readAllChargers(fileString));
            refreshTable();
        } catch (Exception ignored) {
            seng202.team5.views.SupplementaryGUIControllers.Alert.failedToLoadFile();
            return;
        }
    }

    /**
     * Adapted from advanced FX lab
     * Opens the directory browser to allow the user to save their data from the database into a CSV
     */
    @FXML
    public void exportDataToFile() throws NotEnteredException {
        File file;
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Charger CSV File");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            file = fileChooser.showSaveDialog(tableStage);
            fileChooser.setInitialDirectory(new File(MainController.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile());
        } catch (URISyntaxException e) {
            Alert.failedToLoadFile();
            return;
        }

        ArrayList<Charger> chargers = chargerManager.getAllChargers();

        ChargerCSVWriter writer = new ChargerCSVWriter();
        writer.saveRecord(chargers, String.valueOf(file));
    }

    /**
     * Called when the Add Charger button is pressed,
     * Opens a window to allow the user to add a charger to the dataset
     */
    @FXML
    void newCharger() {

        MainController mainController = MainController.getMostRecentMainController();
        mainController.openNewChargerWindow();
    }

    /**
     * Called when the View Charger button is pressed,
     * Opens the window to allow the user to view a charger in the dataset.
     * @throws IOException Exception is thrown when view cannot be opened
     */
    public void viewCharger() {
        try {
            selectedCharger = chargerTableView.getSelectionModel().getSelectedItem();

        } catch (NullPointerException nullPointerException) {
            labelErrorMessage.setText("No Charger Selected");
        }

        if (selectedCharger != null) {
            MainController mainController = MainController.getMostRecentMainController();
            mainController.openViewCharger(selectedCharger);
            ViewChargerController.isMapStage = false;
        } else {
            labelErrorMessage.setText("No Charger Selected");
        }
    }

    /**
     * Will delete a charger if the charger is not null, if the charger is null then it will display an error message
     * with the text "No Charger Selected".
     * @throws IOException if the charger is not able to be read.
     */
    public void deleteCharger() throws IOException {
        ChargerManager chargerManager = new ChargerManager();
        try {
            selectedCharger = chargerTableView.getSelectionModel().getSelectedItem();
        } catch (NullPointerException nullPointerException) {
            labelErrorMessage.setText("No Vehicle Selected");
        }

        if (selectedCharger != null) {
            if (Alert.deleteConfirmation()) {
                chargerManager.delete(selectedCharger);
                chargerManager.update(selectedCharger);
                labelErrorMessage.setText("Charger deleted");
                refreshTable();
            }
        } else {
            labelErrorMessage.setText("No Charger Selected");
        }
    }

    /**
     * Generates and populates the charger table
     */
    private void initChargerTable()  {
        TableManager.initChargerTable(chargerTableView, chargerManager.getAllChargers());
    }

    /**
     * Called whenever new data is added to the table, refreshes the view so that the table is updated
     * @throws IOException Exception is thrown when view cannot be opened
     */
    public void refreshTable() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.init(tableStage, 1);

        tableStage.setTitle("EVOH");
        tableStage.setScene(new Scene(root));
        tableStage.show();
    }

    /**
     * Populates Table with the filtered data
     * @param filters The list of filters to be applied
     */
    public void filterTable(ArrayList<Filter> filters) {
        chargerTableView.getItems().clear();
        ArrayList<Charger> chargers = chargerManager.filter(filters);
        if (filters.isEmpty()) {
            chargerTableView.setItems(FXCollections.observableList(chargerManager.getAllChargers()));
        } else {
            chargerTableView.setItems(FXCollections.observableList(chargers));
        }
    }


    /**
     * Initialises the page
     * @param stage The stage that is being displayed
     */
    public void init(Stage stage) {
        tableStage = stage;
        initChargerTable();
    }

    /**
     * Adds the selected charger from the table to the users favourites
     * Shows an alert if charger is already in the users favourite charger list
     * @throws NotLoggedInException Exception is thrown when user is not logged in
     */
    public void addChargerToFavourites() throws NotLoggedInException {

        if (UserManager.getIsUserLoggedIn()) {
            try {
                selectedCharger = chargerTableView.getSelectionModel().getSelectedItem();
            } catch (NullPointerException nullPointerException) {
                labelErrorMessage.setText("No Charger Selected");
            }
            if (selectedCharger != null) {
                Location location = new Location(selectedCharger.getLatitude(), selectedCharger.getLongitude());
                if (UserManager.getCurrentlyLoggedIn().getFavouriteChargerLocationList().contains(location)) {
                    labelErrorMessage.setText("Charger already in Favourites!");
                } else {
                    UserManager.getCurrentlyLoggedIn().addToFavouriteChargerLocationList(location);
                    addToFavouritesLabel.setVisible(true);

                    PauseTransition visiblePause = new PauseTransition(
                            Duration.seconds(3)
                    );
                    visiblePause.setOnFinished(
                            event -> addToFavouritesLabel.setVisible(false)
                    );
                    visiblePause.play();
                    labelErrorMessage.setText("");
                }
            } else {
                labelErrorMessage.setText("No Charger Selected");
            }


        } else {
            Alert.notLoggedInAlert();
        }
    }
}
