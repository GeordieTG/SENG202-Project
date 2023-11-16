package seng202.team5.models.databaseInteraction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team5.exceptions.DuplicateEntryException;
import seng202.team5.exceptions.NotFoundException;
import seng202.team5.models.data.Journey;
import seng202.team5.models.data.Location;
import seng202.team5.models.data.User;
import seng202.team5.models.data.Vehicle;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User Data Access Object to interact with database.
 * Adapted from Morgan English Example Project.
 */
public class UserDAO implements  DAOInterface<User> {

    /**
     * Calls the application logger
     */
    private static final Logger log = LogManager.getLogger();

    /**
     * Instance of Database manager
     */
    private final DatabaseManager databaseManager;

    /**
     * Creates new instance of gson
     */
    final Gson gson = new Gson();

    /**
     * Creates a new UserDAO object and gets a reference to the database singleton
     */
    public UserDAO(){
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Gets all users in database
     * @return a list of all users
     */
    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try(Connection conn = databaseManager.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Type charger = new TypeToken<ArrayList<Location>>() {}.getType();
                Type journey = new TypeToken<ArrayList<Journey>>() {}.getType();
                Type vehicle = new TypeToken<ArrayList<Vehicle>>() {}.getType();

                ArrayList<Location> favourites = gson.fromJson(rs.getString("favourites"), charger);

                ArrayList<Journey> journeys = gson.fromJson(rs.getString("journeys"), journey);

                ArrayList<Vehicle> vehicles = gson.fromJson(rs.getString("vehicles"), vehicle);

                users.add(new User(rs.getString("username"), rs.getString("password"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), favourites, journeys, vehicles, rs.getFloat("totalCO2"), rs.getFloat("totalDistance")));
            }
            return users;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Adds an individual user to database
     * @param toAdd user to add
     * @return true if no error, false if sql error
     */
    @Override
    public int add(User toAdd) throws DuplicateEntryException {
        String sql = "INSERT INTO users (username, password, first_name, last_name, email, favourites, journeys, vehicles, totalCO2, totalDistance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = databaseManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, toAdd.getUsername());
            ps.setString(2, toAdd.getHashedPassword());
            ps.setString(3, toAdd.getFirstName());
            ps.setString(4, toAdd.getLastName());
            ps.setString(5, toAdd.getEmail());

            // FavouriteChargers
            String chargerIDList = gson.toJson(toAdd.getFavouriteChargerLocationList());
            ps.setString(6, chargerIDList);

            // Journeys
            String journeyList = gson.toJson(toAdd.getSavedJourneyList());
            ps.setString(7, journeyList);

            // Vehicles
            String vehicleList = gson.toJson(toAdd.getVehicleList());

            ps.setString(8, vehicleList);
            ps.setFloat(9, toAdd.getTotalCO2());
            ps.setFloat(10, toAdd.getTotalDistance());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int insertId = -1;
            if (rs.next()) {
                insertId = rs.getInt(1);
            }
            return insertId;
        } catch (SQLException sqlException) {
            if (sqlException.getErrorCode() == 19) {
                throw new DuplicateEntryException("Duplicate username");
            }
            log.error(sqlException);
            return -1;
        }
    }

    /**
     * Deletes user from database by username
     * @param toDelete The user that is being deleted from the database
     */
    @Override
    public void delete(User toDelete) {
        String username = toDelete.getUsername();
        String sql = "DELETE FROM users WHERE username=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Method to update a users records
     * @param toUpdate The user that needs to be updated
     */
    public void update(User toUpdate) {
        String sql = "UPDATE users SET first_name=?, last_name=?, email=?, favourites=?, journeys=?, vehicles=?, totalCO2=?, totalDistance=? WHERE username=?";
        try(Connection conn = databaseManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, toUpdate.getFirstName());
                ps.setString(2, toUpdate.getLastName());
                ps.setString(3, toUpdate.getEmail());

                // FavouriteChargers
                String chargerIDList = gson.toJson(toUpdate.getFavouriteChargerLocationList());
                ps.setString(4, chargerIDList);

                // Journeys
                String journeyList = gson.toJson(toUpdate.getSavedJourneyList());
                ps.setString(5, journeyList);

                // Vehicles
                String vehicleList = gson.toJson(toUpdate.getVehicleList());

                ps.setString(6, vehicleList);
                ps.setFloat(7, toUpdate.getTotalCO2());
                ps.setFloat(8, toUpdate.getTotalDistance());
                ps.setString(9, toUpdate.getUsername());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Gets a single user from database by username, throws not found exception if user does not exist
     * @param username username to search for
     * @return User retrieved with matching username
     * @throws NotFoundException no user found with that username
     */
    public User getFromUserName(String username) throws NotFoundException {
        String sql = "SELECT * FROM users WHERE username=?";
        try(Connection conn = databaseManager.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Type charger = new TypeToken<ArrayList<Location>>() {}.getType();
                Type journey = new TypeToken<ArrayList<Journey>>() {}.getType();
                Type vehicle = new TypeToken<ArrayList<Vehicle>>() {}.getType();

                ArrayList<Location> favourites = gson.fromJson(rs.getString("favourites"), charger);
                ArrayList<Journey> journeys = gson.fromJson(rs.getString("journeys"), journey);
                ArrayList<Vehicle> vehicles = gson.fromJson(rs.getString("vehicles"), vehicle);

                return new User(rs.getString("username"), rs.getString("password"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), favourites, journeys, vehicles, rs.getFloat("totalCO2"), rs.getFloat("totalDistance"));
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
        throw new NotFoundException(String.format("No user with %s found", username));
    }
}
