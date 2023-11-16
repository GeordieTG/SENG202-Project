package seng202.team5.models.databaseInteraction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team5.exceptions.InstanceAlreadyExistsException;
import seng202.team5.exceptions.NotFoundException;
import seng202.team5.models.data.Charger;
import seng202.team5.models.data.Connector;
import seng202.team5.models.data.Location;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Handles all the test cases for ChargerDAO class.
 */
class ChargerDAOTest {

    static ChargerDAO chargerDAO;
    static DatabaseManager databaseManager;

    public Charger defaultCharger() {
        Location location = new Location(43, 140);
        Charger charger = new Charger(location, "UC Charging Station", "Geordie", "Geordie", "77 Ilam Road", true, 100, true, 100, true, 0);
        charger.addConnector(new Connector());
        return charger;
    }

    @BeforeAll
    static void setup() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        chargerDAO = new ChargerDAO();
    }

    @BeforeEach
    void resetDB() {
        databaseManager.resetDB();
    }

    /**
     * Test whether the database starts empty
     */
    @Test
    void testChargersEmptyOnCreation() {
        Assertions.assertEquals(0, chargerDAO.getAll().size());
    }

    /**
     * Tests simply adding one charger to the database
     */
    @Test
    void testDefaultChargerOK() {
        Charger toAdd = defaultCharger();
        defaultCharger().addConnector(new Connector());
        chargerDAO.add(toAdd);
        Assertions.assertEquals(1, chargerDAO.getAll().size());
        Charger charger = chargerDAO.getAll().get(0);

        assertEquals(toAdd.getLocation(), charger.getLocation());
        Assertions.assertEquals(toAdd.getName(), charger.getName());
        Assertions.assertEquals(toAdd.getOperator(), charger.getOperator());
        Assertions.assertEquals(toAdd.getOwner(), charger.getOperator());
        Assertions.assertEquals(toAdd.getAddress(), charger.getAddress());
        Assertions.assertEquals(toAdd.isAlwaysOpen(), charger.isAlwaysOpen());
        Assertions.assertEquals(toAdd.getCarParkCount(), charger.getCarParkCount());
        Assertions.assertEquals(toAdd.isCarParkNeedsPayment(), charger.isCarParkNeedsPayment());
        Assertions.assertEquals(toAdd.getMaxTimeLimit(), charger.getMaxTimeLimit());
        Assertions.assertEquals(toAdd.isTouristAttraction(), charger.isTouristAttraction());
        Assertions.assertEquals(toAdd.getNumberOfConnectors(), charger.getNumberOfConnectors());
        Assertions.assertTrue(toAdd.getConnectorList().get(0).sameType(charger.getConnectorList().get(0)));
    }

    /**
     * Tests deleting a charger
     */
    @Test
    public void testDeleteCharger() {
        Charger charger = defaultCharger();
        chargerDAO.add(charger);
        assertEquals(1, chargerDAO.getAll().size());
        chargerDAO.delete(charger);
        assertEquals(0, chargerDAO.getAll().size());
    }

    /**
     * Tests if a charger can update within the charger database
     */
    @Test
    void testUpdateCharger() {

        // Creates user to put into the database
        Charger toAdd = defaultCharger();

        chargerDAO.add(toAdd);
        toAdd.setName("Jed");
        toAdd.setOwner("Fred");
        chargerDAO.update(toAdd);

        assertEquals("Jed", toAdd.getName());
        assertEquals("Fred", toAdd.getOwner());
    }

    /**
     * Test if the filter function works - Tests for a condition that doesn't exist
     */
    @Test
    void testFilterChargersNone() {
        ArrayList<Filter> filters = new ArrayList<Filter>();
        Filter filter = new Filter("alwaysOpen", "=", "2");
        filters.add(filter);
        ArrayList<Charger> chargers = chargerDAO.filter(filters);
        Assertions.assertEquals(0, chargers.size());
    }

    /**
     * Test if the filter function works - No assertEquals as the amount in the database will very
     */
    @Test
    void testFilterChargers() {
        ArrayList<Filter> filters = new ArrayList<Filter>();
        Filter filter = new Filter("alwaysOpen", "=", "1");
        filters.add(filter);
        chargerDAO.filter(filters);
    }

    @Test
    void testGetFromLatLng() throws SQLException, NotFoundException {
        Charger charger = defaultCharger();
        chargerDAO.add(charger);

        Charger received = chargerDAO.getFromLatLong(43, 140);

        Assertions.assertTrue(charger.getName().equals(received.getName()));
    }

    @Test
    void testAddBatch() {
        Charger charger = defaultCharger();
        Charger charger1 = defaultCharger();

        ArrayList<Charger> batch = new ArrayList<>(Arrays.asList(charger,charger1));

        chargerDAO.addBatch(batch);
    }


}

