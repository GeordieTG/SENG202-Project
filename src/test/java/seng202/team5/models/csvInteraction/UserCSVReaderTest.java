package seng202.team5.models.csvInteraction;

import seng202.team5.models.data.User;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for UserCSVReader
 */
public class UserCSVReaderTest {

    /**
     * Tests the constructor and initializing the reader
     */
    @Test
    public void testConstruction() {

        boolean successful = true;
        try {
            UserCSVReader reader = new UserCSVReader();
            reader.selectFile("src/test/resources/testCSVData/userText.csv");
        } catch (IOException e) {
            successful = false;
        }
        assertTrue(successful);
    }

    /**
     * Tests when an invalid filename is provided to the reader
     */
    @Test
    public void invalidFileNameReadTest() {

        boolean successful = true;

        try {
            UserCSVReader reader = new UserCSVReader();
            reader.selectFile("Invalid/File/Name.csv");
        } catch (IOException e) {
            successful = false;
        }

        assertFalse(successful);
    }


    /**
     * Tests the readAllObjects method (and readNextObject method) for invalid user data
     */
    @Test
    public void readInvalidObjectsTest() {

        boolean successful = true;
        ArrayList<User> users = new ArrayList<>();

        try {
            UserCSVReader reader = new UserCSVReader();
            reader.selectFile("src/main/resources/localCSVData/invalidUser.csv");
            users = reader.readAllUsers();
        } catch (NumberFormatException | EOFException e) {
            successful = false;
        } catch (IOException e) {
            successful = false;
        }
        // Should be false
        assertFalse(successful);
    }
}
