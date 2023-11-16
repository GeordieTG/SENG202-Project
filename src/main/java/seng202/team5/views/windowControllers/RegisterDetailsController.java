package seng202.team5.views.windowControllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team5.managers.UserManager;
import seng202.team5.models.data.User;
import java.io.IOException;

/**
 * Controller for registerDetails.fxml window
 */
public class RegisterDetailsController {

    /**
     * Text field for user's first name
     */
    public TextField firstNameField;

    /**
     * Text field for user's last name
     */
    public TextField lastNameField;

    /**
     * Text field for user's email
     */
    public TextField emailField;
    /**
     * Label to display errors to the user
     */
    public Label labelMessage;

    /**
     * Instance of register details stage
     */
    Stage registerDetailsStage = new Stage();

    /**
     * Calls the application logger
     */
    private static final Logger log = LogManager.getLogger();

    /**
     * When the confirm button is pressed it sets the user to a new user with
     * their required details and then opens the main view.
     * @throws IOException is thrown if main view cannot be opened.
     */
    public void buttonPushed() throws IOException {
        if (User.validateEmail(emailField.getText())) {
            labelMessage.setText("");
            User currentUser = null;
            try {
                currentUser = UserManager.getCurrentlyLoggedIn();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            assert currentUser != null;
            currentUser.setFirstName(firstNameField.getText());
            currentUser.setLastName(lastNameField.getText());
            currentUser.setEmail(emailField.getText());
            UserManager userManger = new UserManager();
            userManger.update(currentUser);
            openMain();
        } else {
            labelMessage.setText("Email is invalid");
        }
    }

    /**
     * Opens the register view.
     * @throws IOException Exception is thrown if register view cannot be opened
     */
    public void backButton() throws IOException {
        openRegister();
    }

    /**
     * Opens the register screen.
     * @throws IOException Exception is thrown if register view cannot be opened
     */
    public void openRegister() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
        Parent root = loader.load();

        RegisterController registerController = loader.getController();
        registerController.init(registerDetailsStage);

        registerDetailsStage.setTitle("Register");
        registerDetailsStage.setScene(new Scene(root));
        registerDetailsStage.show();
    }

    /**
     * Opens the main screen.
     * @throws IOException Exception is thrown if main screen cannot be opened
     */
    public void openMain() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.init(registerDetailsStage, 0);

        registerDetailsStage.setTitle("EVOH");
        registerDetailsStage.setScene(new Scene(root));
        registerDetailsStage.show();
    }

    /**
     * Initialises the registerDetailsStage, if enter is pressed it will act the same as pressing
     * the button.
     * @param stage - controller stage
     */
    public void init(Stage stage) {
        registerDetailsStage = stage;
        emailField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER ) {
                try {
                    buttonPushed();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
