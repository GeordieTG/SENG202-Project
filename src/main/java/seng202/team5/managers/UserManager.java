package seng202.team5.managers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team5.exceptions.DuplicateEntryException;
import seng202.team5.exceptions.NotAuthorizedException;
import seng202.team5.exceptions.NotFoundException;
import seng202.team5.models.data.Location;
import seng202.team5.models.data.User;
import seng202.team5.models.databaseInteraction.UserDAO;
import seng202.team5.exceptions.NotLoggedInException;

/**
 * Manages user uses and interactions.
 * Adapted from Morgan English Example Project.
 */
public class UserManager {

    /**
     * Calls the application logger.
     */
    private static final Logger Log = LogManager.getLogger();

    /**
     * User that stores the user that is currently logged in.
     */
    private static User currentlyLoggedIn = null;

    /**
     * Boolean that represents if a user is logged into the application.
     */
    private static boolean isUserLoggedIn = false;

    /**
     * Location that dictates users current location
     */
    private static Location currentLocation = new Location(-43.5226f, 172.5812f);

    /**
     * Stores the application UserDAO.
     */
    private final UserDAO userDAO;

    /**
     * Creates a new UserManager object and creates a
     * private UserDAO object it will
     * later use for all database interactions.
     */
    public UserManager() {
        userDAO = new UserDAO();
    }

    /**
     * Gets user that is currently logged in.
     * @return The user that is currently logged into the system.
     */
    public static User getCurrentlyLoggedIn() throws NotLoggedInException {
        if (currentlyLoggedIn == null) {
            throw new NotLoggedInException("User is not logged in");
        } else {
            return currentlyLoggedIn;
        }
    }

    /**
     * Sets the logged-in user to the new user.
     * @param user The user to be logged in
     */
    public static void setCurrentlyLoggedIn(final User user) {
        currentlyLoggedIn = user;
        isUserLoggedIn = true;
    }

    /**
     * Manually sets the users current location
     * @param currentLocation the current location of the user.
     */
    public void setCurrentLocation(Location currentLocation) {
        UserManager.currentLocation = currentLocation;
    }

    /**
     * Gets boolean to see if user is currently logged in.
     * @return Boolean that represents if the user is logged in
     */
    public static boolean getIsUserLoggedIn() {
        return isUserLoggedIn;
    }

    /**
     * Gets the users current location that they have manually set
     * @return users current location
     */
    public static Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Basic user authorisation and fetching.
     * Check that username and password match then returns relevant user.
     * @param username username for user
     * @param password password to check for user
     * @return User relevant user object if
     *         authentication of username and password succeeded
     */
    public User authenticateUser(final String username, final String password) {
        try {
            User u = userDAO.getFromUserName(username);

            // Hash login password then make it equals
            HashPassword hashedPassword = new HashPassword(password);

            if ((hashedPassword.getHashedPassword()).equals(u.getHashedPassword())) {
                return u;
            }
            throw new NotAuthorizedException("Password Incorrect");
        } catch (NotAuthorizedException | NotFoundException e) {
            Log.warn(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the user from the database by their username.
     * @param username  username from user input
     * @return Returns the user
     * @throws NotFoundException Exception thrown when username is not found
     */
    public User getUserByUsername(final String username)
            throws NotFoundException {
        return userDAO.getFromUserName(username);
    }

    /**
     * Creates a user from the username and password provided.
     * @param username username to be created
     * @param password password to be created
     */
    public void registerUser(final String username, final String password)
            throws DuplicateEntryException, IllegalArgumentException {
        if (username.equals("") || password.equals("")) {
            throw new IllegalArgumentException("Missing field");
        }

        if (password.length() < 7) {
            throw new IllegalArgumentException("Password too short, Min Length 7");
        }

        HashPassword passHash = new HashPassword(password);
        User user = new User(username, passHash.getHashedPassword(), null,null, null, 0, 0);
        userDAO.add(user);
    }

    /**
     * Updates the current user.
     * @param user current user
     */
    public void update(final User user) {
        userDAO.update(user);
    }

    /**
     * Logs the current user out.
     */
    public static void activeUserLogOut() {
        currentlyLoggedIn = null;
        isUserLoggedIn = false;
    }
}
