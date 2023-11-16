package seng202.team5.models.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Journey class
 */
public class JourneyTest {

    Journey defaultJourney;

    @BeforeEach
    public void setDefaultJourney() {
        ArrayList<Location> stops = new ArrayList<Location>();
        stops.add(new Location(0, 0));
        stops.add(new Location(1, 1));
        defaultJourney = new Journey("Default Journey", stops);
    }

    /**
     * Test to ensure that you cannot create a journey unless there are at least two locations selected
     */
    @Test
    public void CantInstantiateJourneyWithOneStop() {
        ArrayList<Location> stops = new ArrayList<Location>();
        stops.add(new Location(0, 0));
        try {
            Journey journey = new Journey("Journey", stops);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception ignored) {
            fail();
        }
    }

    /**
     * Tests that if add a location to the journey after null it's added to the start of the journey
     */
    @Test
    public void addLocationToStartWithLocationReference() {
        Location newStop = new Location(5,5);
        defaultJourney.addStopAfterLocation(null, newStop);
        assertEquals(newStop, defaultJourney.getStops().get(0));
    }

    /**
     * Test that if you add a stop after a non-existent stop it throws an error
     */
    @Test
    public void addLocationAfterInvalidStopThrowsError() {
        Location newStop = new Location(5, 5);
        try {
            defaultJourney.addStopAfterLocation(new Location(5, 3), newStop);
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    /**
     * Test to make sure that adding a stop doesn't overwrite a previous stop
     */
    @Test
    public void noOverwriteWithNewStop() {
        Location newStop = new Location(5, 5);
        defaultJourney.addStopAfterLocation(defaultJourney.getStops().get(0), newStop);
        assertEquals(3, defaultJourney.getStops().size());
    }

    /**
     * Test to make sure stop is inserted in the correct position
     */
    @Test
    public void stopInsertedInCorrectPosition() {
        Location newStop = new Location(5, 5);
        defaultJourney.addStopAfterLocation(defaultJourney.getStops().get(0), newStop);
        assertEquals(newStop, defaultJourney.getStops().get(1));
    }

    /**
     * Test that stop can be inserted to the back of the journey
     */
    @Test
    public void addStopToEndOfJourney() {
        Location newStop = new Location(5, 5);
        defaultJourney.addStopAfterLocation(defaultJourney.getStops().get(defaultJourney.getStops().size() - 1), newStop);
        assertEquals(newStop, defaultJourney.getStops().get(defaultJourney.getStops().size() - 1));
    }

    /**
     * Test that you can't have more than the max amount of stops
     */
    @Test
    public void testForMaxAmountOfStops() {
        while (true) {
            try {
                defaultJourney.addStopAfterLocation(null, new Location(0, 0));
                if (defaultJourney.getStops().size() > defaultJourney.getMaxJourneyLength()) {
                    fail();
                    break;
                }
            } catch (IllegalStateException ignored) {
                assertEquals(defaultJourney.getMaxJourneyLength(), defaultJourney.getStops().size());
                break;
            }
        }
    }

    /**
     * Test that you can't remove a stop if it would mean the start and end are the same place
     */
    @Test
    public void testMinStopSizeIsTwo() {
        try {
            defaultJourney.removeStop(defaultJourney.getStops().get(0));
            fail();
        } catch (IllegalStateException ignored) {
            assertTrue(true);
        }
    }

    /**
     * Test that you can't remove a stop that isn't within the journey
     */
    @Test
    public void testCantRemoveNonExistingStop() {
        defaultJourney.addStopAfterLocation(null, new Location(5, 5));
        try {
            defaultJourney.removeStop(new Location(3,3));
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    /**
     * Test that the correct station is removed given a valid stop to remove
     */
    @Test
    public void testRemoveStopRemovesCorrectStop() {
        defaultJourney.addStopAfterLocation(null, new Location(3, 3));
        defaultJourney.removeStop(new Location(3, 3));
        assertEquals(2, defaultJourney.getStops().size());
        assertFalse(defaultJourney.getStops().contains(new Location(3, 3)));
    }
}
