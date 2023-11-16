package seng202.team5.views.SupplementaryGUIControllers;

import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Common alerts for the GUI
 */
public class Alert {

    /**
     * Alert to display if a user attempts to access a feature that they must be logged in for
     */
    public static void notLoggedInAlert() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Cannot Open");
        alert.setHeaderText("Please log in to access this feature");
        alert.showAndWait();
    }

    /**
     * An alert to be displayed if a journey name is needed but is not supplied by the user
     */
    public static void noJourneyName() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("No Journey name");
        alert.setHeaderText("Please add a name to the journey");
        alert.showAndWait();
    }

    /**
     * Alert to display whenever data is incorrectly loaded from a csv file
     */
    public static void failedToLoadFile() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("Failed to Open File");
        alert.setHeaderText("Failed to load data from file");
        alert.showAndWait();
    }

    /**
     * Alert to display when an email is sent successfully to the user's email
     */
    public static void emailSentAlert() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Email sent successfully");
        alert.setHeaderText("The email was sent successfully!");
        alert.showAndWait();
    }

    /**
     * Alert to be thrown if a feature needs a route to be selected and there is no route
     */
    public static void noRouteAlert() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("No route found");
        alert.setHeaderText("This feature needs a route to be selected");
        alert.showAndWait();
    }

    /**
     * Alert to display if a user attempts to send an email and the service does not work
     */
    public static void emailServiceDown() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("Email service down");
        alert.setHeaderText("There was an error with your request, please try again later");
        alert.showAndWait();
    }

    /**
     * Displays messages if the charger is already favourited when the user tries to
     * favourite the charger again.
     */
    public static void chargerAlreadyFavourite() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Already a Favourite Charger");
        alert.setHeaderText("This charger is already in your favourites list");
        alert.showAndWait();
    }

    /**
     * Alert to display when the user attempts to view the emergency service on map when no service has been found
     * @param service The service that was being searched for (Hospitals, Fire Stations, Police Stations)
     */
    public static void noEmergencyServiceFound(String service) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("You're Doomed");
        alert.setHeaderText("No " + service + " have been found");
        alert.showAndWait();
    }

    /**
     * Alert to display when the user attempts to add a charger without a connector (all chargers must have at
     * least one connector).
     */
    public static void minimumOneConnector() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Must have Connectors");
        alert.setHeaderText("A charger must have a least one connector, delete the charger if you no longer want it");
        alert.showAndWait();
    }

    /**
     * Alert to display when the user attempts to add a charger a location that already has a charging station.
     */
    public static void invalidChargerLocation() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Invalid Location");
        alert.setHeaderText("A charger already exists with this exact location");
        alert.showAndWait();
    }

    /**
     * Alert to display when the user desires to delete something and is given the opportunity to confirm whether they
     * would like to delete it or not.
     * @return True or False depending on if the user decides to click OK (will return True) or not (will return False).
     */
    public static boolean deleteConfirmation() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
}
