package seng202.team5.views.windows;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng202.team5.views.windowControllers.LoginController;
import javafx.scene.image.Image ;
import java.io.IOException;

/**
 * Class starts the login application window
 */
public class LoginWindow extends Application {

    /**
     * Opens the gui with the fxml content specified in resources/fxml/login.fxml
     * @param primaryStage The current fxml stage, handled by javaFX Application class
     * @throws IOException if there is an issue loading fxml file
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = baseLoader.load();

        LoginController baseController = baseLoader.getController();
        baseController.init(primaryStage);

        Scene scene = new Scene(root);
        primaryStage.getIcons().add(new Image("/images/evoh_logo.png"));
        primaryStage.getIcons().add(new Image("/images/evoh_logo.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Launches the FXML application, this must be called from another class (in this cass App.java) otherwise JavaFX
     * errors out and does not run
     * @param args command line arguments
     */
    public static void main(String [] args) {
        launch(args);
    }

}