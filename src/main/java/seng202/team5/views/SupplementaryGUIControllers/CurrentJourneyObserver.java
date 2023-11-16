package seng202.team5.views.SupplementaryGUIControllers;

/**
 * An interface for everything that needs to listen to the current journey and react on its updates
 */
public interface CurrentJourneyObserver {

    /**
     * Called whenever there is an update to the current route
     */
    void handleJourneyUpdate();

}
