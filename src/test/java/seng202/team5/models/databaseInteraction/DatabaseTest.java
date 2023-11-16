package seng202.team5.models.databaseInteraction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Handles all the test cases for the Data
 * Adapted from Morgan Enlgish Example Project
 */

class DatabaseTest {

    /**
     * Test getInstance function is functional
     */
    @Test
    void testDatabaseManagerInstance() {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        Assertions.assertNotNull(databaseManager);
        Assertions.assertEquals(databaseManager, DatabaseManager.getInstance());
    }

    /**
     * Test database connection is functional
     * @throws SQLException Exception is thrown when there is an error with the database
     */
    @Test
    void testDatabaseConnection() throws SQLException {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        Connection connection = databaseManager.connect();
        Assertions.assertNotNull(connection);
        Assertions.assertNotNull(connection.getMetaData());
        connection.close();
    }
}
