package seng202.team5.models.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team5.views.SupplementaryGUIControllers.CurrentJourneyObserver;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Code to test that the functionality of the Current Journey is consistent including the observer pattern
 */
public class CurrentJourneyObserverTest {

    /**
     * Resets the currentJourney static
     */
    @BeforeEach
    public void resetJourney() {
        Journey.setCurrentJourneySingleton(null);
    }
    /**
     * A temporary class to check whether an alert would be thrown to an observer on the change of the current journey
     */
    static class UpdateCatcher implements CurrentJourneyObserver  {

        /**
         * True when an alert would have been thrown
         */
        private boolean seenUpdate = false;

        /**
         * Called whenever the current journey changes
         */
        @Override
        public void handleJourneyUpdate() {
            seenUpdate = true;
        }

        /**
         * @return Returns if an alert has been thrown to the updateChecker
         */
        public boolean getSeenUpdate() {return seenUpdate;}
    }

    /**
     * Creates a Journey for testing
     * @return The journey to apply on with tests
     */
    public Journey defaultTestJourney() {
        ArrayList<Location> stops = new ArrayList<>();
        stops.add(new Location(5, 5));
        stops.add(new Location(10, 10));
        return new Journey("Test Journey", stops);
    }

    /**
     * Assuming there is no current journey a new journey should be created in its place
     */
    @Test
    void initialiseCurrentJourneyIfNull() {
        Journey.setCurrentJourneySingleton(null);
        assertNotNull(Journey.getCurrentJourneySingleton());
    }

    /**
     * Test that when getting the current journey, it passes a reference to the current journey and not a copy
     */
    @Test
    void getProvidesReferenceAndNotCopy() {
        Journey newJourney = Journey.getCurrentJourneySingleton();
        assertEquals(newJourney, Journey.getCurrentJourneySingleton());
    }

    /**
     * Assert that when you set the current Journey it creates a copy of the journey that is being set
     * This is to stop accidentally editing a saved journey
     */
    @Test
    void setPassesCopyNotReference() {
        Journey newJourney = defaultTestJourney();
        Journey.setCurrentJourneySingleton(newJourney);
        assertNotSame(newJourney, Journey.getCurrentJourneySingleton());
    }


    /**
     * Tests that there is no update thrown to observers if there is
     */
    @Test
    void updateThrownOnJourneyFirstSet() {
        UpdateCatcher updateCatcher = new UpdateCatcher();
        Journey.addCurrentJourneyObserver(updateCatcher);
        Journey.getCurrentJourneySingleton();
        assertTrue(updateCatcher.getSeenUpdate());
    }

    /**
     * Tests that no alert is thrown if the journey doesn't change
     */
    @Test
    void noAlertOnMimicJourney() {
        UpdateCatcher updateCatcher = new UpdateCatcher();
        Journey.setCurrentJourneySingleton(defaultTestJourney());
        Journey.addCurrentJourneyObserver(updateCatcher);
        Journey.setCurrentJourneySingleton(defaultTestJourney());
        assertFalse(updateCatcher.getSeenUpdate());
    }

    /**
     * Tests that an alert is thrown if the journey name has changed
     */
    @Test
    void alertsOnTitleChange() {
        UpdateCatcher updateCatcher = new UpdateCatcher();
        Journey journey = defaultTestJourney();
        Journey.setCurrentJourneySingleton(journey);
        journey.setTitle("Lol");
        Journey.addCurrentJourneyObserver(updateCatcher);
        Journey.setCurrentJourneySingleton(journey);
        assertTrue(updateCatcher.getSeenUpdate());
    }

    /**
     * Test that an alert is thrown when there is a new stop added to the journey
     */
    @Test
    void alertsOnStopChangingToGetJourney() {
        Journey journey = Journey.getCurrentJourneySingleton();
        UpdateCatcher updateCatcher = new UpdateCatcher();
        Journey.addCurrentJourneyObserver(updateCatcher);
        journey.addStopAfterLocation(journey.getStartLocation(), new Location(5, 5));
        assertTrue(updateCatcher.getSeenUpdate());
    }


    /**
     * Alert should be thrown on CO2 change
     */
    @Test
    void alertThrownOnCO2Change() {
        Journey journey = Journey.getCurrentJourneySingleton();
        UpdateCatcher updateCatcher = new UpdateCatcher();
        Journey.addCurrentJourneyObserver(updateCatcher);
        journey.setCO2Emitted(100);
        assertTrue(updateCatcher.getSeenUpdate());
    }
}
