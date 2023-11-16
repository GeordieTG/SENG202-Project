package seng202.team5.models.csvInteraction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import seng202.team5.models.data.*;
import java.io.EOFException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A class which reads csv files containing User data
 */
public class UserCSVReader extends CSVCommon {

    /**
     * Constructor for creating the User csv parser for the application
     */
    public UserCSVReader() {
        super(true);
    }

    /**
     * Reads the next line of user objects within a csv file and creates user.
     *
     * @return User object initialized
     */
    private User readNextUser() throws EOFException, IllegalStateException {

        // Error Handling
        if (this.scanner == null) {
            throw new IllegalStateException("There is no scanner currently active");
        }
        // Error Handling
        if (!this.scanner.hasNextLine()) {
            throw new EOFException("There are no more users to be gathered");
        }

        // Grabs the line
        this.remainingCellString = this.scanner.nextLine();
        // Splits by the commas and stores values in a string array

        Gson gson = new Gson();
        String username = nextCell();
        String password = nextCell();
        String firstName = nextCell();
        String lastName = nextCell();
        String email = nextCell();
        Type LocationArray = new TypeToken<ArrayList<Charger>>() {}.getType();
        ArrayList<Location> favouriteChargers = gson.fromJson(nextCell(), LocationArray);
        Type JourneyArray = new TypeToken<ArrayList<Journey>>() {}.getType();
        ArrayList<Journey> journeys = gson.fromJson(nextCell(), JourneyArray);
        Type VehicleArray = new TypeToken<ArrayList<Vehicle>>() {}.getType();
        ArrayList<Vehicle> vehicles = gson.fromJson( nextCell(), VehicleArray);
        float currentCO2 = Float.parseFloat(nextCell());
        float currentDistance = Float.parseFloat(nextCell());
        return new User(username, password, firstName, lastName, email, favouriteChargers, journeys, vehicles, currentCO2, currentDistance);
    }

    /**
     * Reads all the user objects within a csv file
     * @return ArrayList of users within the csv file
     */
    public ArrayList<User> readAllUsers() throws EOFException {

        ArrayList<User> listOfUsers = new ArrayList<>();

        // Grabs the first line of the csv
        this.scanner.nextLine();

        while (this.scanner.hasNextLine()) {

            // Reads a line and stores the user.
            User theUser = readNextUser();
            // Stores user instance in a list
            listOfUsers.add(theUser);
        }

        // Clear the Reader
        csvClose();

        // Return list of users
        return listOfUsers;

    }
}