package seng202.team5.views.windowControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import seng202.team5.managers.UserManager;
import seng202.team5.models.data.User;
import java.io.IOException;

/**
 * Controller for login.fxml window
 */
public class LoginController {
    Stage loginStage = new Stage();

    /**
     * UserManager is responsible for verifying username and password in this context
     */
    final UserManager userManager = new UserManager();

    /**
     * Text field for the user's username
     */
    @FXML
    private TextField usernameTextField;

    /**
     * Test field for the user's password
     */
    @FXML
    private PasswordField passwordTextField;

    /**
     * Label to display errors to the user
     */
    @FXML
    private Label labelMessage;

    /**
     * Method to call when mapview application window needs to be opened
     */
    public void openMain() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.init(loginStage, 0);

        loginStage.setTitle("EVOH");
        loginStage.setScene(new Scene(root));
        loginStage.setResizable(true);
        loginStage.show();
    }

    /**
     * Method to call when register application window needs to be opened
     */
    public void openRegister() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
        Parent root = loader.load();

        RegisterController registerController = loader.getController();
        registerController.init(loginStage);

        loginStage.setTitle("Register");
        loginStage.setScene(new Scene(root));
        loginStage.show();
    }

    /**
     * Method to call when login is clicked
     */
    public void login() throws IOException {
        if (usernameTextField.getText().isEmpty()){
            labelMessage.setText("Username field is empty");
            return;
        } else if (passwordTextField.getText().isEmpty()) {
            labelMessage.setText("Password field is empty");
            return;
        }
        User user = userManager.authenticateUser(usernameTextField.getText(), passwordTextField.getText());
        if (user == null) {
            labelMessage.setText("Username or Password is incorrect");
        } else {
            UserManager.setCurrentlyLoggedIn(user);
            openMain();
        }
    }

    /**
     * Method to call when guest button is clicked
     */
    public void guest() throws IOException {
        openMain();
    }

    /**
     * Method to call when signup button is clicked
     */
    public void register() throws IOException {
        openRegister();
    }

    /**
     * Initialize the window
     *
     * @param stage Top level container for this window
     */
    public void init(Stage stage) {
        loginStage = stage;
        loginStage.setTitle("EVOH");
        loginStage.sizeToScene();
        stage.setResizable(false);
        passwordTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER ) {
                try {
                    login();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
