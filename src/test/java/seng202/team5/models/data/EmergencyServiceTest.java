package seng202.team5.models.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmergencyServiceTest {

    /**
     * Tests equals method works as expected
     */
    @Test
    void testEqualsMethod() {
        EmergencyService hospital = new EmergencyService("Hospital", 0,0);
        EmergencyService equalHospital = new EmergencyService("Hospital", 0,0);

        Assertions.assertEquals(hospital, equalHospital);
    }

    /**
     * Checks the equals method fails when names are different
     */
    @Test
    void testInvalidNameNEqualsMethod () {
        EmergencyService hospital = new EmergencyService("Hospital", 0,0);
        EmergencyService equalHospital = new EmergencyService("Not Hospital", 0,0);

        Assertions.assertNotEquals(hospital, equalHospital);
    }

    /**
     * Checks the equals method fails when latitudes are different
     */
    @Test
    void testInvalidLatNEqualsMethod () {
        EmergencyService hospital = new EmergencyService("Hospital", 0,0);
        EmergencyService equalHospital = new EmergencyService("Not Hospital", 1,0);

        Assertions.assertNotEquals(hospital, equalHospital);
    }

    /**
     * Checks the equals method fails when longitudes are different
     */
    @Test
    void testInvalidLngNEqualsMethod () {
        EmergencyService hospital = new EmergencyService("Hospital", 0,0);
        EmergencyService equalHospital = new EmergencyService("Hospital", 0,1);

        Assertions.assertNotEquals(hospital, equalHospital);
    }

    /**
     * Test equals method works when an emergency service is compared to a non-emergency service
     */
    @Test
    void testNonEmergencyServiceEqualsMethod() {
        EmergencyService hospital = new EmergencyService("Hospital", 0,0);
        User user = new User();
        Assertions.assertNotEquals(hospital, user);

    }
}
