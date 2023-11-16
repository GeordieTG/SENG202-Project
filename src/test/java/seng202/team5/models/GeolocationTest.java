package seng202.team5.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team5.models.data.EmergencyService;
import seng202.team5.managers.Geolocation;
import seng202.team5.models.data.Location;

/**
 * Tests the Geolocation class
 */
public class GeolocationTest {


    /**
     * Instance of the Geolocation class
     */
    final Geolocation geolocator = new Geolocation();


    /**
     * Tests the expected functionality
     */
    @Test
    void testGetLocation() {
        Location result = geolocator.queryAddress("Christchurch");
        Location location = new Location(-43.530956f, 172.63664f);
        Assertions.assertEquals(result, location);
    }

    /**
     * Boundary Value Checking - Tests invalid address entry
     */
    @Test
    void testGetInvalidLocation() {
        Location result = geolocator.queryAddress("abcdefg");
        Location location = new Location(0, 0);
        Assertions.assertEquals(result, location);
    }

    /**
     * Tests the ability to find the nearest hospital
     */
    @Test
    void testFindNearestHospital() {
        EmergencyService hospital = new EmergencyService("Christchurch Outpatients", -43.53477f, 172.62715f );
        Assertions.assertEquals(geolocator.findClosestHospital(-43.5320f, 172.6306f), hospital);

    }

    /**
     * Tests the ability to find the nearest police station
     */
    @Test
    void testFindNearestPoliceStation() {
        EmergencyService policeStation = new EmergencyService("Christchurch Central Police Station", -43.534435f, 172.63486f);
        Assertions.assertEquals(geolocator.findClosestPoliceStation(-43.5320f, 172.6306f), policeStation);
    }

    /**
     * Tests the ability to find the nearest fire station
     */
    @Test
    void testFindNearestFireStation() {
        EmergencyService fireStation = new EmergencyService("Christchurch City Fire Station", -43.527004f, 172.64406f);
        Assertions.assertEquals(geolocator.findClosestFireStation(-43.5320f, 172.6306f), fireStation);
    }

    /**
     * Tests the error handling when the nearest emergency station can't be located
     */
    @Test
    void testCannotFindNearestEmergencyStation() {
        Assertions.assertNull(geolocator.findClosestFireStation(0, 0));

    }

    /**
     * Tests the reverse geolocation functionality (getting address from lat/lng)
     */
    @Test
    void testGetAddressFromLocation() {
        String address = geolocator.getAddressFromLocation(-43.527004f, 172.64406f);
        Assertions.assertEquals(address, "Christchurch City Fire Station Kilmore Street Central City");
    }

    /**
     * Tests the error handling when an address cannot be found from a given lat/lng
     */
    @Test
    void testCannotGetAddressFromLocation() {
        String address = geolocator.getAddressFromLocation(0.1f, 0f);
        Assertions.assertEquals(address, "");

    }
}
