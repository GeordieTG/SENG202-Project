package seng202.team5.models.data;

import seng202.team5.exceptions.NotFoundException;
import seng202.team5.exceptions.NotLoggedInException;
import seng202.team5.managers.ChargerManager;
import seng202.team5.managers.UserManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for a User which stores information such as favourite chargers, saved journeys and registered cars
 */

public class User {

    /**
     * Default User Constructor
     */
    public User(){}

    /**
     * constructor for default user
     * @param username User's username
     * @param password User's password
     */
    public User (String username, String password) {
        setUsername(username);
        setHashedPassword(password);
    }

    /**
     * Constructor for a User
     * @param username User's username
     * @param password User's password
     * @param firstName User's First Name
     * @param lastName User's Last Name
     * @param email User's email
     * @param favourites User's favourite chargers
     * @param journeys User's saved journeys
     * @param vehicles User's vehicles
     * @param totalCO2 User's total CO2 emissions
     * @param totalDistance User's total distance travelled
     * @throws IllegalArgumentException Exception thrown when data is entered in wrong format
     */
    public User (String username, String password, String firstName, String lastName, String email, ArrayList<Location> favourites, ArrayList<Journey> journeys, ArrayList<Vehicle> vehicles, float totalCO2, float totalDistance) throws IllegalArgumentException {
        setUsername(username);
        setHashedPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.favouriteChargerLocationList = favourites;
        this.savedJourneyList = journeys;
        this.vehicleList = vehicles;
        this.totalCO2 = totalCO2;
        this.totalDistance = totalDistance;
    }


    /**
     * Additional User Constructor
     * @param username The user's username
     * @param password The user's password
     * @param firstName The user's first name
     * @param lastName The user's last name
     * @param email The user's email
     * @param totalC02 The user's total CO2 emissions
     * @param totalDistance The user's total distance travelled
     */
    public User(String username, String password, String firstName, String lastName, String email, int totalC02, int totalDistance) {
        setUsername(username);
        setHashedPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.totalCO2 = totalC02;
        this.totalDistance = totalDistance;
    }



// Variables

    /**
     * Users username
     */
    private String username = null;

    /**
     * Users password
     */
    private String hashedPassword = null;
    
    /**
     * Users first name
     */
    private String firstName = null;
    /**
     * Users last name
     */
    private String lastName = null;
    /**
     * Users email
     */
    private String email = null;
    /**
     * List of users favourite charger ID's
     */
    private ArrayList<Location> favouriteChargerLocationList = new ArrayList<>();

    /**
     * List of users saved Journeys
     */
    private ArrayList<Journey> savedJourneyList = new ArrayList<>();
    /**
     * List of users registered vehicles
     */
    private ArrayList<Vehicle> vehicleList = new ArrayList<>();
    /**
     * Users total CO2 emitted
     */
    private float totalCO2 = 0;
    /**
     * Users total distance travelled
     */
    private float totalDistance = 0;

    /**
     * User Manager used to perform database updates
     */
    final UserManager userManager = new UserManager();

// Setters

    /**
     * Sets the users first name to passed through string
     * @param firstName The user's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets users last name to passed through string
     * @param lastName The user's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    /**
     * Sets the user's vehicle list to the passed through Array List
     * @param vehicleList List of the user's vehicles
     */
    public void setVehicleList(ArrayList<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    /**
     * Sets the users saved journeys list to the passed through Array List
     * @param savedJourneyList List of the user's saved journeys
     */
    public void setSavedJourneyList(ArrayList<Journey> savedJourneyList) {
        this.savedJourneyList = savedJourneyList;
    }


// Getters

    /**
     * Get the users saved journeys
     * @return a list of the users saved journeys
     */
    public ArrayList<Journey> getSavedJourneyList() {
        return savedJourneyList;
    }

    /**
     * Get the users registered cars
     * @return a list of the users registered cars
     */
    public ArrayList<Vehicle> getCarList() {
        return vehicleList;
    }

    /**
     * Gets the user's first name
     * @return users first name as a string
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the user's last name
     * @return users last name as a string
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the user's email
     * @return users email as a string
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's total CO2 emissions
     * @return users total CO2 emissions as a float
     */
    public float getTotalCO2() {
        return totalCO2;
    }
    /**
     * Gets the User's total distance travelled
     * @return users total distance as a float
     */
    public float getTotalDistance() {
        return totalDistance;
    }

    /**
     * Gets the user's username
     * @return users username as a string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username
     * @param username The username of the user
     */
    public void setUsername(String username) throws IllegalArgumentException {
        if (username.length() >= 4) {
            this.username = username;
        } else {
            throw new IllegalArgumentException("Username too short. Min length 4.");
        }
    }

    /**
     * Gets the user's saved vehicles
     * @return list of users vehicles
     */
    public ArrayList<Vehicle> getVehicleList() {
        return vehicleList;
    }

// Methods

    /**
     * Adds the given journey to the users saved journey list
     * @param journey A journey the user wants to save
     */
    public void addJourney(Journey journey) throws IllegalArgumentException {
        Journey journeyToSave = journey.copy();
        for (Journey oldJourney : this.savedJourneyList) {
            if (journeyToSave.getTitle().equals(oldJourney.getTitle())) {
                throw new IllegalArgumentException("Duplicate Journey Name" + oldJourney.getTitle() + " " + journey.getTitle());
            }
        }
        savedJourneyList.add(journeyToSave);
        try {
            userManager.update(UserManager.getCurrentlyLoggedIn());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the given vehicle to the users registered vehicle list
     * @param vehicle A vehicle that the user wants to register
     */
    public void addVehicle(Vehicle vehicle) {
        vehicleList.add(vehicle);
    }

    /**
     * Gets the user's hashed password
     * @return the users hashed password
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Sets the user's password (hashed) to the given string
     * @param hashedPassword A password that the user want to add to their details
     */
    public void setHashedPassword(String hashedPassword) {
        if (hashedPassword.length() >= 7) {
            this.hashedPassword = hashedPassword;
        } else {
            throw new IllegalArgumentException("Password too short, Min length 7");
        }
    }

    /**
     * Gets a list of IDs of the user's favourite chargers
     * @return the users favourite charger list
     */
    public ArrayList<Location> getFavouriteChargerLocationList() {
        return favouriteChargerLocationList;
    }

    /**
     * Sets the list of the IDs of the users favourite chargers to the given Array List
     * @param favouriteChargerLocationList A list of the user's favourite charger IDs
     */
    public void setFavouriteChargerLocationList(ArrayList<Location> favouriteChargerLocationList) {
        this.favouriteChargerLocationList = favouriteChargerLocationList;
    }

    /**
     * Sets the user's email to the given string
     * @param email The user's email
     */
    public void setEmail(String email) {
        if (validateEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid Email");
        }
    }

    /**
     * Determines whether the email is valid or not.
     * Regex source: Email Validation in Java. (2022). Baeldung. Retrieved, from https://www.baeldung.com/java-email-validation-regex
     * @return - true or false depending on if it is a valid email or not.
     */
    public static boolean validateEmail(String emailInput) {
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(emailInput);
        return matcher.find();
    }

    /**
     * Method to check if two different instances of user are equal. Overides default .equal()
     * @param o object being compared to
     * @return true if equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof User)) {
            return false;
        }

        User c = (User) o;

        return (firstName.equals(c.getFirstName()))
                && (lastName.equals(c.getLastName()))
                && (username.equals(c.getUsername()))
                && (email.equals(c.getEmail()))
                && (savedJourneyList.equals(c.getSavedJourneyList()))
                && (vehicleList.equals(c.getVehicleList()))
                && (totalCO2 == c.getTotalCO2())
                && (totalDistance == c.getTotalDistance());
    }

    /**
     * Add a Charger to the users favourite charger list
     * @param location location of the charger wanting to be favourited
     * @throws NotLoggedInException when the user is not logged in
     */
    public void addToFavouriteChargerLocationList(Location location) throws NotLoggedInException {
        favouriteChargerLocationList.add(location);
        userManager.update(UserManager.getCurrentlyLoggedIn());
    }

    /**
     * Converts the users list of chargers (which are currently stored as a list of locations) to Charger objects.
     * @return an ArrayList of users favourite chargers as Charger objects.
     * @throws SQLException if the SQL query throws errors.
     * @throws NotFoundException when the charger is not found.
     */
    public ArrayList<Charger> favouriteChargersToObjects() throws SQLException, NotFoundException {
        ArrayList<Charger> chargers = new ArrayList<Charger>();

        ChargerManager chargerManager = new ChargerManager();
        for (Location location : favouriteChargerLocationList) {
            Charger current = chargerManager.getFromLatLong(location.getLatitude(), location.getLongitude());
            chargers.add(current);
        }
        return chargers;
    }

    /**
     * Removes a given charger from the users favourite charger list.
     * @param charger the charger to be removed.
     * @throws NotLoggedInException when the user is not logged in.
     */
    public void removeChargerFromFavourites(Charger charger) throws NotLoggedInException {
        favouriteChargerLocationList.remove(charger.getLocation());
        userManager.update(UserManager.getCurrentlyLoggedIn());
    }
}
