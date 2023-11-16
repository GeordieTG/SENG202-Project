package seng202.team5.models.databaseInteraction;

import seng202.team5.exceptions.DuplicateEntryException;
import java.util.List;

/**
 * Interface for both DAO classes
 * Heavily influenced from Morgan Enlgish Example Project
 */
public interface DAOInterface<T> {

    /**
     * Gets all of T from the database
     * @return List of all objects type T from the database
     */
    List<T> getAll();

    /**
     * Adds a single object of type T to database
     * @param toAdd object of type T to add
     * @return object insert id if inserted correctly
     * @throws DuplicateEntryException if the object already exists
     */
    int add(T toAdd) throws DuplicateEntryException;

    /**
     * Deletes and object from database that matches id given
     * @param toDelete The primary key for the given table to delete from
     */
    void delete(T toDelete);

    /**
     * Updates an object in the database
     * @param toUpdate Object that needs to be updated (this object must be able to identify itself and its previous self)
     */
    void update(T toUpdate);
}