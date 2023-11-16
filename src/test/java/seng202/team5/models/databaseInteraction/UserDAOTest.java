package seng202.team5.models.databaseInteraction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team5.exceptions.DuplicateEntryException;
import seng202.team5.exceptions.InstanceAlreadyExistsException;
import seng202.team5.exceptions.NotFoundException;
import seng202.team5.exceptions.NotLoggedInException;
import seng202.team5.managers.UserManager;
import seng202.team5.models.data.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Handles all the test cases for ChargerDAO class.
 */

class UserDAOTest {
    static UserDAO userDAO;
    static DatabaseManager databaseManager;
    private static int databaseInitialSize;

// Morgan's Tests

    /**
     * Sets up variables for use in the rest of the tests
     * @throws InstanceAlreadyExistsException Exception is thrown when database instance already exists
     */
    @BeforeAll
    static void setup() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        userDAO = new UserDAO();
        databaseInitialSize = userDAO.getAll().size();
    }

    /**
     * Resets the database before every test
     */
    @BeforeEach
    void resetDB() {
        databaseManager.resetDB();
    }

    /**
     * Tests that the database starts empty when it is called
     */
    @Test
    void testDatabaseStartsEmpty() {
        assertEquals(0, userDAO.getAll().size());
    }

    /**
     * Tests that adding a default user is functional
     * @throws DuplicateEntryException Exception is thrown if data is already in the database
     */
    @Test
    void testAddDefaultUserOK() throws DuplicateEntryException {
        assertEquals(databaseInitialSize, userDAO.getAll().size());
        User toAdd = new User("username", "password");
        userDAO.add(toAdd);
        assertEquals(1, userDAO.getAll().size());
        User user = userDAO.getAll().get(0);
        assertEquals(toAdd.getUsername(), user.getUsername());
        assertEquals(toAdd.getHashedPassword(), user.getHashedPassword());
    }

    /**
     * Tests that getting a user by username is functional
     * @throws NotFoundException Exception is thrown if data is not found in database
     * @throws DuplicateEntryException Exception is thrown if data is already in the database
     */
    @Test
    void testGetUserByUsername() throws NotFoundException, DuplicateEntryException {
        User toAdd = new User("test user", "password");
        userDAO.add(toAdd);
        User user = userDAO.getFromUserName("test user");
        assertEquals(user.getUsername(), toAdd.getUsername());
        assertEquals(user.getHashedPassword(), toAdd.getHashedPassword());
    }

    /**
     * Tests that when getting a username that is not in the database, it gives the correct response
     */
    @Test
    void testGetUserByUsernameThrowsNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> userDAO.getFromUserName("A username that doesnt exist"));
    }

    /**
     * Tests that delete user is functional
     * @throws DuplicateEntryException Exception is thrown if data is already in the database
     */
    @Test
    void testDeleteUser() throws DuplicateEntryException{
        int currentDatabaseSize =  userDAO.getAll().size();
        User user = new User("test user", "password");
        userDAO.add(user);
        assertEquals(currentDatabaseSize + 1, userDAO.getAll().size());
        userDAO.delete(user);
        assertEquals(currentDatabaseSize, userDAO.getAll().size());
    }

    /**
     * Test whether duplicate entries work as expected
     * @throws DuplicateEntryException Exception is thrown if data is already in the database
     */
    @Test
    void testDuplicateUser() throws DuplicateEntryException{
        userDAO.add(new User("duplicate", "password"));
        Assertions.assertThrows(DuplicateEntryException.class, () -> userDAO.add(new User("duplicate", "password")));
    }

// Our Tests

    /**
     * Creates a user with all the necessary data
     * @return The created user
     */
    User completeUser() {
        User user = new User("username", "password", "Geordie", "Gibson", "ggi28@uclive.ac.nz", 200, 1000);
        Location location1 = new Location(0,0);
        Location location2 = new Location(1,1);
        ArrayList<Location> chargers = new ArrayList<>(Arrays.asList(location1, location2));
        user.setFavouriteChargerLocationList(chargers);

        Journey j = new Journey();
        Journey j1 = new Journey();
        Journey j2 = new Journey();
        ArrayList<Journey> journeys = new ArrayList<>(Arrays.asList(j,j1,j2));
        user.setSavedJourneyList(journeys);

        Vehicle v = new Vehicle();
        Vehicle v2 = new Vehicle();
        Vehicle v3 = new Vehicle();
        ArrayList<Vehicle> vehicles = new ArrayList<>(Arrays.asList(v,v2,v3));
        user.setVehicleList(vehicles);

        return user;
    }

    /**
     * Test whether the user can be added and pulled from the database
     * @throws DuplicateEntryException Exception is thrown if data is already in the database
     */
    @Test
    void testAddAndPullUserComplete() throws DuplicateEntryException {
        assertEquals(databaseInitialSize, userDAO.getAll().size());
        // Creates user to put into the database
        User toAdd = completeUser();
        userDAO.add(toAdd);

        assertEquals(databaseInitialSize + 1, userDAO.getAll().size());

        User user = userDAO.getAll().get(0);
        assertEquals(user, toAdd);
    }

    /**
     * Test whether the user can be pulled from the database by username
     * @throws NotFoundException Exception is thrown if data is not found in database
     * @throws DuplicateEntryException Exception is thrown if data is already in the database
     */
    @Test
    void testGetUserByUsernameComplete() throws NotFoundException, DuplicateEntryException {
        assertEquals(databaseInitialSize, userDAO.getAll().size());
        // Creates user to put into the database
        User toAdd = completeUser();
        userDAO.add(toAdd);

        User user = userDAO.getFromUserName("username");
        assertEquals(user, toAdd);
    }

    /**
     * Test whether the user can be updated
     * @throws DuplicateEntryException Exception is thrown if data is already in the database
     */
    @Test
    void testUpdateCompleteUser() throws DuplicateEntryException {
        assertEquals(databaseInitialSize, userDAO.getAll().size());

        // Creates user to put into the database
        User toAdd = completeUser();

        userDAO.add(toAdd);
        toAdd.setFirstName("Jed");
        toAdd.setLastName("Fred");
        userDAO.update(toAdd);

        assertEquals("Jed", toAdd.getFirstName());
        assertEquals("Fred", toAdd.getLastName());
    }

    /**
     * Tests if a user can successfully add a charger to its favourites list
     */
    @Test
    void testAddChargerToFavouriteChargers() throws NotLoggedInException, NotFoundException, DuplicateEntryException {
        // Create new user and add it to database
        User user = completeUser();
        UserManager.setCurrentlyLoggedIn(user);

        userDAO.add(user);

        // Create new charger and add it to the users favourite charger list
        Location location = new Location(1,1);
        user.addToFavouriteChargerLocationList(location);

        // Pull user from database and see if it has been updated
        User pulled = userDAO.getFromUserName("username");
        Assertions.assertEquals(pulled.getFavouriteChargerLocationList(), user.getFavouriteChargerLocationList());
    }

    /**
     * Tests to see if a user can successfully remove a charger from their favourite charger list
     */
    @Test
    void testRemoveChargerFromFavouriteChargers() throws DuplicateEntryException, NotFoundException, NotLoggedInException {
        // Create new user and add it to database
        User user = completeUser();
        userDAO.add(user);
        UserManager.setCurrentlyLoggedIn(user);

        // Remove charger for user's favourite charger list
        Charger charger = new Charger();
        charger.setLocation(new Location(1,1));
        user.removeChargerFromFavourites(charger);

        // Pull user from database and see if it has been updated
        User pulled = userDAO.getFromUserName("username");
        Assertions.assertEquals(pulled.getFavouriteChargerLocationList(), user.getFavouriteChargerLocationList());
    }
}