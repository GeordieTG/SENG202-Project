package seng202.team5.models.databaseInteraction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team5.exceptions.NotFoundException;
import seng202.team5.models.data.*;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Charger Data Access Object to interact with database.
 * Heavily influenced by Morgan English's Demo Project.
 */
public class ChargerDAO implements DAOInterface<Charger> {

    /**
     * Gives access to database functionality
     */
    private final DatabaseManager databaseManager;

    /**
     * Calls the application logger
     */
    private static final Logger log = LogManager.getLogger();

    /**
     * Creates new instance of gson
     */
    final Gson gson = new Gson();

    /**
     * Creates a new ChargerDAO object and gets a reference to the database singleton
     */
    public ChargerDAO(){
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Will get a charger from the latitude and longitude.
     * @param lat - latitude of the charger
     * @param lon - longitude of the charger
     * @return a charger from the given lat and long.
     * @throws NotFoundException if a charger is not found.
     * @throws SQLException if the SQL statement provides errors.
     */
    public Charger getFromLatLong(float lat, float lon) throws NotFoundException, SQLException {
        String sql = "SELECT * FROM chargers WHERE latitude=? AND longitude=?";
        Charger charger;
        try (Connection conn = databaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setFloat(1, lat);
            stmt.setFloat(2, lon);
            ResultSet rs = stmt.executeQuery();
            {

                while (rs.next()) {
                    charger = new Charger(
                            new Location(rs.getFloat("latitude"), rs.getFloat("longitude")),
                            rs.getString("name"),
                            rs.getString("operator"),
                            rs.getString("owner"),
                            rs.getString("address"),
                            rs.getBoolean("alwaysOpen"),
                            rs.getInt("carParkCount"),
                            rs.getBoolean("carParkNeedsPayment"),
                            rs.getInt("maxTimeLimit"),
                            rs.getBoolean("touristAttraction"),
                            rs.getInt("rating"));
                    Type connector = new TypeToken<ArrayList<Connector>>() {
                    }.getType();
                    ArrayList<Connector> connectors = gson.fromJson(rs.getString("connectors"), connector);
                    for (Connector newConnector : connectors) {
                        charger.addConnector(newConnector);
                    }
                    return charger;
                }
            }
        }

        throw new NotFoundException("No charger found with that location");
    }


    /**
     * Gets all chargers in database
     * @return a list of all chargers
     */
    @Override
    public ArrayList<Charger> getAll() {
        ArrayList<Charger> chargers = new ArrayList<>();
        String sql = "SELECT * FROM chargers";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Charger charger = new Charger(new Location(rs.getFloat("latitude"),
                        rs.getFloat("longitude")),
                        rs.getString("name"),
                        rs.getString("operator"),
                        rs.getString("owner"),
                        rs.getString("address"),
                        rs.getBoolean("alwaysOpen"),
                        rs.getInt("carParkCount"),
                        rs.getBoolean("carParkNeedsPayment"),
                        rs.getInt("maxTimeLimit"),
                        rs.getBoolean("touristAttraction"),
                        rs.getInt("rating"));
                Type connector = new TypeToken<ArrayList<Connector>>() {}.getType();
                ArrayList<Connector> connectors = gson.fromJson(rs.getString("connectors"), connector);
                for (Connector newConnector : connectors) {
                    charger.addConnector(newConnector);
                }
                chargers.add(charger);
            }
            return chargers;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }

    }

    /**
     * @param filters list of filter objects
     * @return a list of all chargers from the database that meet the filtered criteria
     */
    public ArrayList<Charger> filter(ArrayList<Filter> filters) {

        StringBuilder sql = new StringBuilder("SELECT * FROM chargers");
        int index = 0;

        for (Filter filter : filters) {

            String column = filter.getColumn();
            String value = filter.getValue();
            String operator = filter.getOperator();

            if (index == 0) {
                sql.append(" WHERE ").append(column).append(" ").append(operator).append(" ").append(value);
            } else {
                sql.append(" AND ").append(column).append(" ").append(operator).append(" ").append(value);
            }
            index = 1;

        }
        sql.append(";");

        ArrayList<Charger> chargers = new ArrayList<>();
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {
            while (rs.next()) {
                chargers.add(new Charger(
                        new Location(rs.getFloat("latitude"), rs.getFloat("longitude")),
                        rs.getString("name"),
                        rs.getString("operator"),
                        rs.getString("owner"),
                        rs.getString("address"),
                        rs.getBoolean("alwaysOpen"),
                        rs.getInt("carParkCount"),
                        rs.getBoolean("carParkNeedsPayment"),
                        rs.getInt("maxTimeLimit"),
                        rs.getBoolean("touristAttraction"),
                        rs.getInt("rating")));
            }
            return chargers;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Adds an individual charger to the database
     * @param charger charger to add
     * @return -1 if error when adding, 0 if successful
     */
    @Override
    public int add(Charger charger) {
        String sql = "INSERT OR IGNORE INTO chargers (latitude, longitude, name, operator, owner, address, alwaysOpen, carParkCount, carParkNeedsPayment, maxTimeLimit, touristAttraction, numberOfConnectors, connectors, rating) values (?, ?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try (Connection conn = databaseManager.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            configurePreparedStatementForCharger(ps, charger);
            ps.executeUpdate();
            return 0;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return -1;
        }
    }

    /**
     * Adds a batch of chargers to the database
     * @param toAdd list of chargers to add to the database
     */
    public void addBatch(List<Charger> toAdd) {
        String sql = "INSERT OR IGNORE INTO chargers (latitude, longitude, name, operator, owner, address, alwaysOpen, carParkCount, carParkNeedsPayment, maxTimeLimit, touristAttraction, numberOfConnectors, connectors, rating) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try (Connection conn = databaseManager.connect()) {
            conn.setAutoCommit(false);
            for (Charger charger : toAdd) {
                PreparedStatement ps = conn.prepareStatement(sql);
                configurePreparedStatementForCharger(ps, charger);
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Abstraction to configure the prepared statement for the next charger
     * @param ps Prepared statement
     * @param charger The charger to configure
     * @throws SQLException Thrown if there is an issue with the SQL
     */
    private void configurePreparedStatementForCharger(PreparedStatement ps, Charger charger) throws SQLException {
        ps.setFloat(1, charger.getLocation().getLatitude());
        ps.setFloat(2, charger.getLocation().getLongitude());
        ps.setString(3, charger.getName());
        ps.setString(4, charger.getOperator());
        ps.setString(5, charger.getOwner());
        ps.setString(6, charger.getAddress());
        ps.setBoolean(7, charger.isAlwaysOpen());
        ps.setInt(8, charger.getCarParkCount());
        ps.setBoolean(9, charger.isCarParkNeedsPayment());
        ps.setInt(10, charger.getMaxTimeLimit());
        ps.setBoolean(11, charger.isTouristAttraction());
        ps.setInt(12, charger.getNumberOfConnectors());
        ps.setString(13, gson.toJson(charger.getConnectorList()));
        ps.setInt(14, charger.getRating());
    }

    /**
     * Deletes charger from database by the primary key (Location)
     * @param charger the charger to delete from the database
     */
    @Override
    public void delete(Charger charger) {
        Location chargerLocation = charger.getLocation();
        String sql = "DELETE FROM chargers WHERE latitude=? AND longitude=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setFloat(1, chargerLocation.getLatitude());
            ps.setFloat(2, chargerLocation.getLongitude());
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Updates the charger in database
     * @param charger The charger to update in database
     */
    @Override
    public void update(Charger charger) {
        String sql = "UPDATE chargers SET name=?, operator=?, owner=?,address=?, alwaysOpen=?,carParkCount=?,carParkNeedsPayment=?,maxTimeLimit=?,touristAttraction=?,numberOfConnectors=?,connectors=?,rating=? WHERE latitude=? AND longitude=?";
        try(Connection conn = databaseManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, charger.getName());
            ps.setString(2, charger.getOperator());
            ps.setString(3, charger.getOwner());
            ps.setString(4, charger.getAddress());
            ps.setBoolean(5, charger.isAlwaysOpen());
            ps.setInt(6, charger.getCarParkCount());
            ps.setBoolean(7, charger.isCarParkNeedsPayment());
            ps.setInt(8, charger.getMaxTimeLimit());
            ps.setBoolean(9, charger.isTouristAttraction());
            ps.setInt(10, charger.getNumberOfConnectors());
            ps.setString(11, gson.toJson(charger.getConnectorList()));
            ps.setInt(12, charger.getRating());
            ps.setFloat(13, charger.getLocation().getLatitude());
            ps.setFloat(14, charger.getLocation().getLongitude());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

