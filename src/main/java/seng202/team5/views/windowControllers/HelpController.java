package seng202.team5.views.windowControllers;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Controller for help.fxml window
 */
public class HelpController {
   /**
     * The JavaFX stage for the help screen.
     */
    Stage helpStage = new Stage();


    /**
     * JavaFX treeView for the FAQ section in the help page.
     */
    @FXML
    private TreeView<Text> treeView;

    /**
     * Initializes the JavaFX window for the help screen.
     * @param stage Top level container for this window
     */
    public void init(Stage stage) {
        helpStage = stage;
        populateFAQ();
    }

    /**
     * This function will convert a string to text with our required font and size.
     * @param string the given string (what you wish to convert).
     * @return the String converted to Text with the required dimensions.
     */
    private Text convertToText(String string) {
        Text text = new Text();
        text.setText(string);
        text.setStyle("-fx-font: 18 system");
        text.setFill(Paint.valueOf("#797474"));
        text.setFont(Font.font("systems", FontWeight.NORMAL, FontPosture.REGULAR, 18));
        return text;
    }

    /**
     * This function populates the FAQ treeView in the help controller.
     */
    private void populateFAQ() {
        TreeItem<Text> dummyRoot = new TreeItem<>(convertToText(""));

        TreeItem<Text> markersQuestion = new TreeItem<>(convertToText("What do the markers on the map mean?"));
        ImageView blackMarkerIcon = new ImageView(String.valueOf((getClass().getResource("/images/black_marker.png"))));
        TreeItem<Text> blackMarkers = new TreeItem<>(convertToText("Black markers represent your current location."), blackMarkerIcon);
        ImageView blueMarkerIcon = new ImageView(String.valueOf((getClass().getResource("/images/blue_marker.png"))));
        TreeItem<Text> blueMarkers = new TreeItem<>(convertToText("Blue markers represent your journey stops."), blueMarkerIcon);
        ImageView greenMarkerIcon = new ImageView(String.valueOf((getClass().getResource("/images/green_marker.png"))));
        TreeItem<Text> greenMarkers = new TreeItem<>(convertToText("Green markers represent charging stations."), greenMarkerIcon);
        ImageView redMarkerIcon = new ImageView(String.valueOf((getClass().getResource("/images/red_marker.png"))));
        TreeItem<Text> redMarkers = new TreeItem<>(convertToText("Red markers represents your search location."), redMarkerIcon);
        ImageView yellowMarkerIcon = new ImageView(String.valueOf((getClass().getResource("/images/yellow_marker.png"))));
        TreeItem<Text> yellowMarkers = new TreeItem<>(convertToText("Yellow markers represent favourited chargers."), yellowMarkerIcon);

        // vehicleIcon from:
        // https://www.flaticon.com/free-icon/car_3202926?term=vehicle&page=1&position=5&page=1&position=5&related_id=3202926&origin=search
        ImageView vehicleIcon = new ImageView(String.valueOf((getClass().getResource("/images/vehicle.png"))));
        TreeItem<Text> addVehicleQuestion = new TreeItem<>(convertToText("How do I add a vehicle?"), vehicleIcon);
        TreeItem<Text> howToAddVehicle = new TreeItem<>(convertToText("First you have to log in then click the " +
                "'REGISTER VEHICLE' button"));
        TreeItem<Text> howToAddVehicle2 = new TreeItem<>(convertToText(" and enter your cars' details!"));

        TreeItem<Text> seeVehiclesInfo = new TreeItem<>(convertToText("How do I see my vehicles?"));
        TreeItem<Text> seeVehicles = new TreeItem<>(convertToText("As long as you are logged in you will be able"
                + " to view your"));
        TreeItem<Text> seeVehicles2 = new TreeItem<>(convertToText(" vehicles by clicking 'PROFILE' and then clicking 'Vehicles'."));

        TreeItem<Text> evohMeaningQuestion = new TreeItem<>(convertToText("What does EVOH mean?"));
        TreeItem<Text> evohMeaning = new TreeItem<>(convertToText("Electric Vehicle Online Help"));

        TreeItem<Text> currentLocationQuestion = new TreeItem<>(convertToText("How do I change my current location?"));
        TreeItem<Text> currentLocation = new TreeItem<>(convertToText("On 'MAP VIEW', double click anywhere on the map or enter"));
        TreeItem<Text> currentLocation2 = new TreeItem<>(convertToText("the address into the search bar"));

        TreeItem<Text> guestRestrictionsQuestion = new TreeItem<>(convertToText("What features am I restricted from as a guest?"));
        TreeItem<Text> guestRestrictions = new TreeItem<>(convertToText("Guests cannot access their profile, register vehicle, email or"));
        TreeItem<Text> guestRestrictions2 = new TreeItem<>(convertToText("save journeys, or favourite chargers"));

        TreeItem<Text> planJourneyQuestion = new TreeItem<>(convertToText("How do I plan a journey?"));
        TreeItem<Text> planJourney = new TreeItem<>(convertToText("When you are logged in, click the 'PLAN JOURNEY' button on"));
        TreeItem<Text> planJourney2 = new TreeItem<>(convertToText("the 'MAP VIEW'. Next select your journey start and end"));
        TreeItem<Text> planJourney3 = new TreeItem<>(convertToText("by dragging the markers where needed. Add stops to a"));
        TreeItem<Text> planJourney4 = new TreeItem<>(convertToText("journey by typing in an address in the given field. Users"));
        TreeItem<Text> planJourney5 = new TreeItem<>(convertToText("can also save and email journeys by clicking the"));
        TreeItem<Text> planJourney6 = new TreeItem<>(convertToText("appropriate button."));

        TreeItem<Text> addChargerQuestion = new TreeItem<>(convertToText("How do I add a new charger?"));
        TreeItem<Text> addCharger = new TreeItem<>(convertToText("On 'TABLE VIEW' add a charger by clicking the 'ADD CHARGER'"));
        TreeItem<Text> addCharger2 = new TreeItem<>(convertToText("button and fill in the available fields."));

        TreeItem<Text> editChargerQuestion = new TreeItem<>(convertToText("How do I edit an existing charger?"));
        TreeItem<Text> editCharger = new TreeItem<>(convertToText("Access the 'View Charger' window by either clicking on a"));
        TreeItem<Text> editCharger2 = new TreeItem<>(convertToText("charger and selected 'View Charger' on 'MAP VIEW' or clicking"));
        TreeItem<Text> editCharger3 = new TreeItem<>(convertToText("the 'VIEW CHARGER' button on 'TABLE VIEW'. From this screen"));
        TreeItem<Text> editCharger4 = new TreeItem<>(convertToText("click the 'EDIT CHARGER' button and enter changes in the"));
        TreeItem<Text> editCharger5 = new TreeItem<>(convertToText("appropriate fields and click the 'SAVE' button to save changes."));

        TreeItem<Text> changePasswordQuestion = new TreeItem<>(convertToText("Can I change my password?"));
        TreeItem<Text> changePassword = new TreeItem<>(convertToText("Currently we have no way to change your password."));

        TreeItem<Text> viewJourneysQuestion = new TreeItem<>(convertToText("How do I view my previous journeys?"));
        TreeItem<Text> viewJourneys = new TreeItem<>(convertToText("To view your previously saved journeys, by clicking on the"));
        TreeItem<Text> viewJourneys2 = new TreeItem<>(convertToText("'PROFILE' button and then the 'Previous Journeys' button."));

        TreeItem<Text> viewFavouriteChargersQuestion = new TreeItem<>(convertToText("How do I view my favourite chargers"));
        TreeItem<Text> viewFavouriteChargers = new TreeItem<>(convertToText("To view your favourite chargers, by clicking on the 'PROFILE'"));
        TreeItem<Text> viewFavouriteChargers2 = new TreeItem<>(convertToText("button and then the 'Favourite Chargers' button."));

        TreeItem<Text> userDetailsQuestion = new TreeItem<>(convertToText("How do I change my user details?"));
        TreeItem<Text> userDetails = new TreeItem<>(convertToText("Edit your user details by clicking on the 'PROFILE' button and"));
        TreeItem<Text> userDetails2 = new TreeItem<>(convertToText("then click the 'View Details' button and then click the"));
        TreeItem<Text> userDetails3 = new TreeItem<>(convertToText("'Edit Profile' button to make changes."));


        markersQuestion.getChildren().addAll(blackMarkers, blueMarkers, redMarkers, greenMarkers, yellowMarkers);
        addVehicleQuestion.getChildren().addAll(howToAddVehicle, howToAddVehicle2);
        evohMeaningQuestion.getChildren().addAll(evohMeaning);
        seeVehiclesInfo.getChildren().addAll(seeVehicles, seeVehicles2);
        currentLocationQuestion.getChildren().addAll(currentLocation, currentLocation2);
        guestRestrictionsQuestion.getChildren().addAll(guestRestrictions, guestRestrictions2);
        planJourneyQuestion.getChildren().addAll(planJourney, planJourney2, planJourney3, planJourney4, planJourney5, planJourney6);
        addChargerQuestion.getChildren().addAll(addCharger, addCharger2);
        editChargerQuestion.getChildren().addAll(editCharger, editCharger2, editCharger3, editCharger4, editCharger5);
        changePasswordQuestion.getChildren().addAll(changePassword);
        viewJourneysQuestion.getChildren().addAll(viewJourneys, viewJourneys2);
        viewFavouriteChargersQuestion.getChildren().addAll(viewFavouriteChargers, viewFavouriteChargers2);
        userDetailsQuestion.getChildren().addAll(userDetails, userDetails2, userDetails3);

        dummyRoot.getChildren().addAll(markersQuestion, addVehicleQuestion, seeVehiclesInfo, evohMeaningQuestion, currentLocationQuestion, guestRestrictionsQuestion,
                planJourneyQuestion, addChargerQuestion, editChargerQuestion, changePasswordQuestion, viewJourneysQuestion, viewFavouriteChargersQuestion, userDetailsQuestion);

        treeView.setRoot(dummyRoot);
        treeView.setShowRoot(false);
    }
}
