package seng202.team5.managers;

import org.junit.jupiter.api.Test;
import seng202.team5.models.data.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests base log in authentication and adding a user to the database
 */
public class UserManagerTest {

    /**
     * Tests that when a user is logged in, all user managers see the same logged-in user
     */
    @Test
    public void testStaticLoggedInUser() {
        User testUser = new User("TestUser", "TestPassword");
        UserManager firstManager = new UserManager();
        UserManager.setCurrentlyLoggedIn(testUser);
        UserManager secondManager = new UserManager();
        try {
            assertEquals(testUser, UserManager.getCurrentlyLoggedIn());
        } catch (Exception ignored) {fail();}
    }
}
