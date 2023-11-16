package seng202.team5.managers;

import seng202.team5.exceptions.NotFoundException;
import seng202.team5.models.data.Charger;
import seng202.team5.models.databaseInteraction.Filter;
import seng202.team5.models.databaseInteraction.ChargerDAO;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Manages charger uses and interactions.
 * Adapted from Morgan English's Example Project.
 */

public class ChargerManager {

    /**
     * Access to the Charger database functionality.
     */
    private final ChargerDAO chargerDAO;

    /**
     * ChargerManager constructor creates new instance of ChargerDAO.
     */
    public ChargerManager() {
        chargerDAO = new ChargerDAO();
    }

    /**
     * Adds a Charger.
     * @param charger charger to add
     */
    public void addCharger(final Charger charger) {
        chargerDAO.add(charger);
    }

    /**
     * Adds each charger within an arrayList into the database.
     * @param chargers The array of chargers to add
     */
    public void addBatch(final ArrayList<Charger> chargers) {
        chargerDAO.addBatch(chargers);
    }

    /**
     * Gets all chargers.
     * @return a list of all chargers stored in the charger database
     */
    public ArrayList<Charger> getAllChargers() {
        return chargerDAO.getAll();
    }

    public Charger getFromLatLong(float lat, float lon) throws SQLException, NotFoundException {
        return chargerDAO.getFromLatLong(lat,lon);
    }


    /**
     * Updates the current charger.
     * @param charger current charger
     */
    public void update(final Charger charger) {
        chargerDAO.update(charger);
    }


    /**
     * Calls the database to delete a charger
     * @param charger The charger to delete
     */
    public void delete(Charger charger) {
        chargerDAO.delete(charger);
    }

    /**
     * Returns a filtered list of chargers.
     * @param filters a list of filter objects
     * @return Filtered List of chargers that meet the filter criteria
     */
    public ArrayList<Charger> filter(final ArrayList<Filter> filters) {
        return chargerDAO.filter(filters);
    }
}
