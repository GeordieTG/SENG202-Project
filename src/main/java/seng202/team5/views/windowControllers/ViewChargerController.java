package seng202.team5.views.windowControllers;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import seng202.team5.models.data.Charger;
import seng202.team5.views.SupplementaryGUIControllers.JavaScriptBridge;
import seng202.team5.views.SupplementaryGUIControllers.jsConnectorInitCaller;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Controller for the viewCharger.fxml window
 */
public class ViewChargerController extends jsConnectorInitCaller {

    /**
     * Label to display Charger Name
     */
    @FXML
    private Label chargerName;

    /**
     * Label to display Charger Latitude
     */
    @FXML
    private Label chargerLatitude;

    /**
     * Label to display Charger Longitude
     */
    @FXML
    private Label chargerLongitude;

    /**
     * Label to display Charger Operator
     */
    @FXML
    private Label chargerOperator;

    /**
     * Label to display Charger Owner
     */
    @FXML
    private Label chargerOwner;

    /**
     * Label to display Charger Address
     */
    @FXML
    private Label chargerAddress;

    /**
     * Label to display Charger Max Use Time
     */
    @FXML
    private Label chargerTime;

    /**
     * Label to display if charger needs payment
     */
    @FXML
    private Label chargerPayment;

    /**
     * Label to display if charger is always open
     */
    @FXML
    private Label chargerOpen;

    /**
     * Label to display if there is a tourist attraction nearby
     */
    @FXML
    private Label chargerAttraction;

    /**
     * Label to display charger connectors
     */
    @FXML
    private Label chargerConnector;

    /**
     * Label to display number of carparks.
     */
    @FXML
    private Label chargerCarpark;

    /**
     * Button to display the editCharger button.
     */
    @FXML
    private Button editCharger;

    /**
     * Button to display the backButton.
     */
    @FXML
    private Button backButton;

    /**
     * Boolean to determine if it is the map stage or not. 1 if it is the map stage, 0 if it is not the map stage.
     */
    public static boolean isMapStage;

    /**
     * Web view to show rating stars
     */
    @FXML
    private WebView ratingView;

    /**
     * Web engine for webview
     */
    private WebEngine ratingWebEngine = null;

    /**
     * Javascript bridge to call functions from html file
     */
    private final JavaScriptBridge javaScriptBridge = new JavaScriptBridge();

    /**
     * Current charger to view
     */
    private Charger viewedCharger;

    /**
     * The Javascript object within the leaflet_osm_map.html that controls the Javascript of the map
     * jsConnector.call("exampleFunction") is how we call Javascript functions from Java
     */
    private JSObject jsConnector;

    /**
     * Set relevant labels to show selected charger data
     * @param charger Charger selected by user
     */
    public void showData(Charger charger) {
        viewedCharger = charger;
        chargerName.setText(charger.getName());
        chargerLatitude.setText(String.valueOf(charger.getLatitude()));
        chargerLongitude.setText(String.valueOf(charger.getLongitude()));
        chargerOperator.setText(charger.getOperator());
        chargerOwner.setText(charger.getOwner());
        chargerAddress.setText(charger.getAddress());
        chargerCarpark.setText(String.valueOf(charger.getCarParkCount()));
        chargerOpen.setText(String.valueOf(charger.isAlwaysOpen()));
        chargerPayment.setText(String.valueOf(charger.isCarParkNeedsPayment()));
        chargerAttraction.setText(String.valueOf(charger.isTouristAttraction()));

        if (charger.getMaxTimeLimit() == 2147483647) {
            chargerTime.setText("Unlimited");
        } else {
            chargerTime.setText(String.valueOf(charger.getMaxTimeLimit()));
        }

        if (charger.isAlwaysOpen()) {
            chargerOpen.setText("This charger is always open");
        } else {
            chargerOpen.setText("This charger is NOT always open");
        }

        if (charger.isCarParkNeedsPayment()) {
            chargerPayment.setText("This charger requires payment to use");
        } else {
            chargerPayment.setText("This charger is free to use");
        }

        if (charger.isTouristAttraction()) {
            chargerAttraction.setText("This charger has a tourist attraction nearby");
        } else {
            chargerAttraction.setText("This charger is NOT near a tourist attraction");
        }

    }

    /**
     * Will open the edit charger window.
     */
    public void editCharger() {
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openEditCharger(viewedCharger);
    }

    /**
     * Initialises the page,
     * Calls showData method
     */
    public void init(Charger toView) {
        MainController.getMostRecentMainController().getStage().sizeToScene();
        this.viewedCharger = toView;
        showData(toView);
        loadRate(toView);
    }

    /**
     * Will load at the given rate.
     */
    public void loadRate(Charger charger) {
        ratingWebEngine = ratingView.getEngine();
        ratingWebEngine.setJavaScriptEnabled(true);
        ratingWebEngine.loadContent(getRatingHTML());

        ratingWebEngine.getLoadWorker().stateProperty().addListener(
                (ov, oldState, newState) -> {
                    // if javascript loads successfully
                    if (newState == Worker.State.SUCCEEDED) {
                        // set our bridge object
                        JSObject window = (JSObject) ratingWebEngine.executeScript("window");
                        window.setMember("javaScriptBridge", javaScriptBridge);
                        // get a reference to the js object that has a reference to the js methods we test need to use in java
                        jsConnector = (JSObject) ratingWebEngine.executeScript("jsConnector");
                        // call the javascript function to initialise the map
                        jsConnector.call("toggle", charger.getRating());


                    }
                });
    }

    /**
     * Will get the HTML star rating and return it as a string
     * @return the rating as a string.
     */
    private String getRatingHTML() {
        InputStream is = getClass().getResourceAsStream("/html/review_star.html");
        assert is != null;
        return new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    /**
     * Opens view connector page
     */
    public void viewConnectorInformation() {
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openViewConnectors(viewedCharger);
    }

    /**
     * When the back button is clicked it will determine if the user came from the map stage or from the table stage. If
     * isMapStage == 1 then this function will open the map screen otherwise, it will open the table screen.
     */
    public void back() {
        if (isMapStage) {
            addMapInitCommand("viewChargerOnMap");
            Object[] objects = new Object[]{viewedCharger.getLatitude(), viewedCharger.getLongitude()};
            addMapInitParams(objects);
            startMap();
        } else {
            MainController mainController = MainController.getMostRecentMainController();
            mainController.openTableWindow();
        }
    }
}
