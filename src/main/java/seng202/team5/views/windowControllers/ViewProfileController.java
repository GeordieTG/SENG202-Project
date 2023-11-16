package seng202.team5.views.windowControllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team5.managers.UserManager;
import seng202.team5.models.data.User;

/**
 * Controller for viewProfile.fxml
 */
public class ViewProfileController {

    /**
     * Text field for username
     */
    @FXML
    public Text usernameText;
    /**
     * Text field for first name
     */
    @FXML
    public Text firstNameText;
    /**
     * Text field for last name
     */
    @FXML
    public Text lastNameText;
    /**
     * Text field for email text
     */
    @FXML
    public Text emailText;
    /**
     * Stage for the view profile
     */
    Stage viewProfileStage = new Stage();


    /**
     * Calls the application logger
     */
    private static final Logger log = LogManager.getLogger();

    /**
     * Opens the "edit profile" view
     */
    public void openEditProfileWindow() {
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openEditProfile();
    }

    /**
     * Opens the settings view
     */
    @FXML
    public void openSettingsWindow() {
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openSettingsWindow();
    }

    /**
     * Sets the texts for the fields when viewing profile
     */
    public void setTexts() {
        User user = null;
        try {
            user = UserManager.getCurrentlyLoggedIn();
            usernameText.setText(String.format("%s", user.getUsername()));
            firstNameText.setText(String.format("%s", user.getFirstName()));
            lastNameText.setText(String.format("%s", user.getLastName()));
            emailText.setText(String.format("%s", user.getEmail()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Initializes the view profile stage
     * @param stage - The stage of the view profile controller
     */
    public void init(Stage stage) {
        viewProfileStage = stage;
        setTexts();
    }
}

