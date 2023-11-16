package seng202.team5.views.windowControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import seng202.team5.managers.UserManager;
import seng202.team5.models.data.User;
import seng202.team5.models.databaseInteraction.UserDAO;

/**
 * Controller for the editProfile.fxml window
 */
public class EditProfileController {

    /**
     * The text field for the user's username
     */
    public TextField newUsernameText;

    /**
     * The text field for the user's first name
     */
    public TextField newFirstNameText;

    /**
     * The text field for the user's last name
     */
    public TextField newLastNameText;

    /**
     * The text field for the user's email
     */
    public TextField newEmailText;

    /**
     * The Label that displays any errors to the user
     */
    public Label testLabel;


    /**
     * Edit Profile Stage
     */
    Stage editProfileStage = new Stage();


    /**
     * opens the viewProfile window
     * @param event - back button event
     */
    @FXML
    void openViewProfileWindow(ActionEvent event) {
        MainController mainController = MainController.getMostRecentMainController();
        mainController.openViewProfile();
    }

    /**
     * updates the profile details when given input
     */
    public void updateProfileDetails() {
        UserManager userManager = new UserManager();
        User user;
        User newUser;
        UserDAO userDatabase = new UserDAO();

        try {
            user = UserManager.getCurrentlyLoggedIn();
            if (!newUsernameText.getText().isEmpty()) {

                newUser = new User(newUsernameText.getText(), user.getHashedPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getFavouriteChargerLocationList(), user.getSavedJourneyList(), user.getVehicleList(), user.getTotalCO2(), user.getTotalDistance());
                userDatabase.add(newUser);
                UserManager.setCurrentlyLoggedIn(newUser);

                // Deletes previous user
                userDatabase.delete(user);
                testLabel.setText("Saved");
                testLabel.setVisible(true);


                if (!newFirstNameText.getText().isEmpty()) {

                    newUser.setFirstName(newFirstNameText.getText());
                    userManager.update(newUser);
                    testLabel.setText("Saved");
                    testLabel.setVisible(true);
                }
                if (!newLastNameText.getText().isEmpty()) {

                    newUser.setLastName(newLastNameText.getText());
                    userManager.update(newUser);
                    testLabel.setText("Saved");
                    testLabel.setVisible(true);
                }

                if (!newEmailText.getText().isEmpty()) {

                    if (User.validateEmail(newEmailText.getText())) {
                        newUser.setEmail(newEmailText.getText());
                        userManager.update(newUser);
                        testLabel.setText("Saved");
                        testLabel.setVisible(true);
                    } else {
                        testLabel.setText("Invalid Email!");
                        testLabel.setVisible(true);
                    }
                }
            } else if (newUsernameText.getText().isEmpty()) {

                user = UserManager.getCurrentlyLoggedIn();


                if (!newFirstNameText.getText().isEmpty()) {

                    user.setFirstName(newFirstNameText.getText());
                    userManager.update(user);
                    testLabel.setText("Saved");
                    testLabel.setVisible(true);
                }
                if (!newLastNameText.getText().isEmpty()) {

                    user.setLastName(newLastNameText.getText());
                    userManager.update(user);
                    testLabel.setText("Saved");
                    testLabel.setVisible(true);
                }
                if (!newEmailText.getText().isEmpty()) {

                    if (User.validateEmail(newEmailText.getText())) {
                        user.setEmail(newEmailText.getText());
                        userManager.update(user);
                        testLabel.setText("Saved");
                        testLabel.setVisible(true);
                    } else {
                        testLabel.setText("Invalid Email!");
                        testLabel.setVisible(true);
                    }
                }
            }
        } catch (Exception e) {
            testLabel.setText(e.getMessage());
            testLabel.setVisible(true);
        }
    }

    /**
     * Initialises the editProfileStage
     * @param stage - controller stage
     */
    public void init(Stage stage) {
        editProfileStage = stage;
        newEmailText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER ) {
                updateProfileDetails();
            }
        });
    }
}
