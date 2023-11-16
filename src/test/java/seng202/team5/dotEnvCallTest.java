package seng202.team5;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Primarily to check that a value exists within the .env for the given keys
 * WARNING: Doesn't check the key is correct, just that the developer has placed a key within the database
 */
public class dotEnvCallTest {

    final Dotenv dotenv = Dotenv.load();

    /**
     * Tests that the email password is in the .env file
      */
    @Test
    public void emailPasswordTest() {
        assertNotNull(dotenv.get("EMAIL_PASSWORD"));
    }

    /**
     * Tests that the EVRoam key is within the .env file
     */
    @Test
    public void evRoamAPIKeyTest() {
        assertNotNull(dotenv.get("EVROAM_API_KEY"));
    }
}
