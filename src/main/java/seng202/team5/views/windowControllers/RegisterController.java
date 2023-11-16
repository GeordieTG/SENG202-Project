package seng202.team5.views.windowControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import seng202.team5.managers.UserManager;
import java.io.IOException;

/**
 * Controller for register.fxml window
 */
public class RegisterController {
    /**
     * Instance of register stage
     */
    Stage registerStage = new Stage();



    /**
     * Label to display errors to the user
     */
    @FXML
    private Label labelErrorMessage;

    /**
     * Text field for the user's username
     */
    @FXML
    private TextField usernameTextField;

    /**
     * Text field for the user's password
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Text field for the user to confirm password
     */
    @FXML
    private PasswordField passwordConfirmField;

    /**
     * The UserManager that is able to interact with the database
     */
    final UserManager userManager = new UserManager();

    /**
     * Method to call when login application window needs to be opened
     */
    void openLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();

        LoginController loginController = loader.getController();
        loginController.init(registerStage);

        registerStage.setTitle("Login");
        registerStage.setScene(new Scene(root));
        registerStage.show();
    }

    /**
     * Method to call when register button is clicked
     * Checks to see if all boxes are filled and the passwords are the same
     */
    public void register() {
        if(usernameTextField.getText().isEmpty()) {
            labelErrorMessage.setText("Username field is empty");
        } else if(passwordField.getText().isEmpty()) {
            labelErrorMessage.setText("Password field is empty");
        } else if(passwordConfirmField.getText().isEmpty()) {
            labelErrorMessage.setText("Confirm Password field is empty");
        } else if(!(passwordField.getText().equals(passwordConfirmField.getText()))) {
            labelErrorMessage.setText("Passwords do not match");
        } else {
            try {
                userManager.registerUser(usernameTextField.getText(), passwordField.getText());
                UserManager.setCurrentlyLoggedIn(userManager.getUserByUsername(usernameTextField.getText()));
                labelErrorMessage.setText("User registered successfully");
                openDetails();
            } catch (Exception e) {
                labelErrorMessage.setText(e.getMessage());
            }

        }

    }

    /**
     * Method to open register details view in the current stage
     * @throws IOException Exception is thrown when view cannot be opened
     */
    public void openDetails() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registerDetails.fxml"));
        Parent root = loader.load();

        RegisterDetailsController registerDetailsController = loader.getController();
        registerDetailsController.init(registerStage);

        registerStage.setTitle("Register Details");
        registerStage.setScene(new Scene(root));
        registerStage.show();
    }

    /**
     * Method to call when back button is clicked
     */
    public void back() throws IOException {
        openLogin();
    }

    /**
     * Initialize the register window
     *
     * @param stage Top level container for this window
     */
    public void init(Stage stage) {
        registerStage = stage;
        passwordConfirmField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER ) {
                register();
            }
        });
    }
}
