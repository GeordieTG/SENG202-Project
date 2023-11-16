package seng202.team5.managers;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import seng202.team5.exceptions.IncorrectConfigForEnvException;
import seng202.team5.exceptions.NoRouteException;
import seng202.team5.models.data.User;
import seng202.team5.exceptions.NotLoggedInException;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.BodyPart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.ArrayList;
import seng202.team5.views.SupplementaryGUIControllers.JavaScriptBridge;
import seng202.team5.views.SupplementaryGUIControllers.JourneyDrawerController;

/**
 * Manager that prepares and send emails using the Javax.mail library.
 */
public final class EmailManager {

    /**
     * Removes instantiation of the Email Manager class.
     */
    private EmailManager() {

    }

    /**
     * Prepares an HTML element for the directions of the route.
     * @return String representing the HTML element for the route
     * @throws NoRouteException Thrown if there isn't a route to display
     */
    public static String prepareDirectionsString() throws NoRouteException {
        ArrayList<String> directionArray = JavaScriptBridge
                .getCurrentRouteInstructions();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<table><tbody>");
        int counter = 0;
        for (String direction : directionArray) {
            counter++;
            stringBuilder.append("<tr><td>");
            stringBuilder.append(counter);
            stringBuilder.append(": ");
            stringBuilder.append(direction);
            stringBuilder.append("</tr></td>");
        }
        stringBuilder.append("</table></tbody>");
        return stringBuilder.toString();
    }

    /**
     * Generates and sends the email to the user.
     * @param imageDirectory The directory of the image to
     *                       be attached to the route email ("") for no image
     * @throws MessagingException Thrown if there is an error with the
     *                       javax.mail library
     * @throws NotLoggedInException Thrown if there is no
     *                       user to send the email to
     * @throws NoRouteException Thrown if there is no route to send to the email
     * @throws IllegalArgumentException Thrown if the user doesn't have an email
     */
    public static void sendRouteToUser(final String imageDirectory)
            throws MessagingException, NotLoggedInException,
            NoRouteException, IncorrectConfigForEnvException {
        String emailUser = "seng202team5@gmail.com";
        Properties prop = new Properties();
        prop.put("mail.user", emailUser);
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.debug", "false"); // You're welcome Morgan ;)
        prop.put("mail.smtp.auth", "true");

        Dotenv dotenv = Dotenv.load();
        String emailPassword = dotenv.get("EMAIL_PASSWORD");
        if (emailPassword == null) {
            throw new IncorrectConfigForEnvException("EMAIL_PASSWORD");
        }

        User currentUser = UserManager.getCurrentlyLoggedIn();
        String currentUserEmail = currentUser.getEmail();
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication
                    getPasswordAuthentication() {
                        return new PasswordAuthentication(emailUser,
                                emailPassword);
                    }
                });
        session.setDebug(false); // You're welcome Morgan ;)

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailUser));
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(currentUserEmail));
        message.setSubject("Directions to your next destination");
        MimeMultipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = "<H1>Here is the route to your destination:</H1>"
                + prepareDirectionsString();
        if (!imageDirectory.equals("")) {
            htmlText += "<img src=\"cid:image\">";
        }
        messageBodyPart.setContent(htmlText, "text/html");
        multipart.addBodyPart(messageBodyPart);
        if (!imageDirectory.equals("")) {
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource(imageDirectory);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(messageBodyPart);
        }
        message.setContent(multipart);
        Transport.send(message);
        Platform.runLater(JourneyDrawerController::setLabelToSentEmail);
    }

}
