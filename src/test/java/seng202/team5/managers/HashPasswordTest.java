package seng202.team5.managers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing for HashPassword
 */
public class HashPasswordTest {

    /**
     * Tests that the HashPassword class is initialized correctly given password
     */
    @Test
    public void testInitializesHashPassword() {

        boolean successful = true;
        try {
            HashPassword hash = new HashPassword("password");
        } catch (Exception e) {
            successful = false;
        }

        assertTrue(successful);
    }

    /**
     * Tests that hashing the same password equals the same string
     */
    @Test
    public void testEqualsHashPassword() {

        HashPassword hashedP = new HashPassword("Test");
        HashPassword testP = new HashPassword("Test");
        assertEquals(hashedP.getHashedPassword(), testP.getHashedPassword());

    }

    /**
     * Tests a password and comparing it to another password that is very similar but not the same
     */
    @Test
    public void testAlmostEqualsPassword() {

        HashPassword hashP = new HashPassword("Test");
        HashPassword testP = new HashPassword("test");

        assertNotEquals(hashP.getHashedPassword(), testP.getHashedPassword());

    }

    /**
     * Tests that the hashing algorithm matches the expected output when hashed
     */
    @Test
    public void testCorrectHashing() {

        // Hashed version of "password"
        String hashCorrect = "5f4dcc3b5aa765d61d8327deb882cf99";

        HashPassword hashP = new HashPassword("password");

        assertEquals((hashP.getHashedPassword()), hashCorrect);

    }
}
