package seng202.team5.models.data;

import seng202.team5.views.SupplementaryGUIControllers.CurrentJourneyObserver;
import seng202.team5.managers.UserManager;
import java.util.ArrayList;

/**
 * This class is used for representing user journeys (Temporary, until final).
 * The other Journey class will be deleted.
 */
public class Journey {

    /**
     * The current journey that is being planned / displayed
     */
    private static Journey currentJourneySingleton = null;

    /**
     * The list of listeners of the current journey
     */
    private static ArrayList<CurrentJourneyObserver> currentJourneyObservers = new ArrayList<CurrentJourneyObserver>();

    /**
     * The max amount of stops that can be within a journey
     */
    private static final int MAX_JOURNEY_LENGTH = 5;

    /**
     * The title of the journey
     */
    private String title;

    /**
     * The stops along the way of the journey
     */
    private ArrayList<Location> stops = new ArrayList<>();

    /**
     * The amount of C02 Emitted for that journey
     */
    private float CO2Emitted;

    /**
     * Default start location for the current journey
     */
    private static final Location DEFAULT_START_LOCATION = new Location(-43.5225f, 172.5794f);


    /**
     * Default stop location for the current journey
     */
    private static final Location DEFAULT_STOP_LOCATION = new Location(-43.5299f, 172.6333f);

    /**
     * Default constructor that instantiates a Journey with start and end at 0,0
     */
    public Journey() {
        title = "New Journey";
        stops = new ArrayList<Location>();
        stops.add(DEFAULT_START_LOCATION);
        stops.add(DEFAULT_STOP_LOCATION);
        CO2Emitted = 0;
    }

    /**
     * The Constructor for a journey class
     * @param title The title of the journey
     * @param stops The stops of the journey
     */
    public Journey(String title, ArrayList<Location> stops) throws IllegalArgumentException {
        this.title = title;
        if (stops.size() < 2) {
            throw new IllegalArgumentException("Stops must have at least 2 stops to be a Journey");
        } else {
            this.stops = stops;
        }
        this.CO2Emitted = 0;
    }

    /**
     * The Constructor for a journey class
     * @param title The title of the journey
     * @param stops The stops of the journey
     */
    public Journey(String title, ArrayList<Location> stops, float CO2Emitted) throws IllegalArgumentException {
        this.title = title;
        if (stops.size() < 2) {
            throw new IllegalArgumentException("Stops must have at least 2 stops to be a Journey");
        } else {
            this.stops = stops;
        }
        this.CO2Emitted = CO2Emitted;
    }

    /**
     * Adds the Observer to the list of observers
     * @param observer The new observer
     */
    public static void addCurrentJourneyObserver(CurrentJourneyObserver observer) {
        currentJourneyObservers.add(observer);
    }





    /**
     * Changes the current journey to a copy of the journey passed to it
     * @param journey The new journey
     */
    public static void setCurrentJourneySingleton(Journey journey) {
        if (journey != null) {
            if(journey.equals(currentJourneySingleton)) {
                return;
            }
            currentJourneySingleton = journey.copy();
        } else {
            currentJourneySingleton = null;
        }
        alertObservers();
    }

    /**
     * Resets the Journey Singleton to NULL
     */
    public static void resetJourneySingleton() {
        currentJourneySingleton = null;
    }

    /**
     * Alerts the observers of a change in the route
     */
    private static void alertObservers() {
        for (CurrentJourneyObserver observer : currentJourneyObservers) {
            observer.handleJourneyUpdate();
        }
    }

    /**
     * Creates a copy of the journey for saving
     * @return A copy of the journey
     */
    public Journey copy() {
        Journey newJourney = new Journey();
        newJourney.setTitle(title);
        newJourney.setStops(stops);
        newJourney.setCO2Emitted(CO2Emitted);
        return newJourney;
    }

    /**
     * @return Returns the currentSingletonJourney, if there is no journey at the moment then it generates a new journey
     */
    public static Journey getCurrentJourneySingleton() {
        if (currentJourneySingleton == null) {
            UserManager user = new UserManager();
            ArrayList<Location> tempStops = new ArrayList<Location>();

            Location startLocation = user.getCurrentLocation();
            tempStops.add(startLocation);
            Location endLocation = new Location(startLocation.getLatitude(), (startLocation.getLongitude() + 0.01f));

            tempStops.add(endLocation);
            setCurrentJourneySingleton(new Journey("New Journey", tempStops));
        }
        return currentJourneySingleton;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }
        Journey second = (Journey) o;
        return stops.equals(second.getStops()) && title.equals(second.getTitle()) && CO2Emitted == second.getCO2Emitted();
    }
    /**
     * Gets the title of the journey
     * @return The title of the journey
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the stops within the journey
     * @return ArrayList containing the locations of the stops in the journey
     */
    public ArrayList<Location> getStops() {
        return stops;
    }

    /**
     * Gets the C02Emitted for a journey
     * @return Float value representing the CO2 Emitted during journey
     */
    public float getCO2Emitted() {
        return CO2Emitted;
    }

    /**
     * Sets the title of the journey
     * @param title The desired title for the journey
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the CO2 emitted of the journey
     * @param CO2Emitted The amount of CO2 Emitted for the journey
     */
    public void setCO2Emitted(float CO2Emitted) throws IllegalArgumentException{

        if (CO2Emitted < 0) {
            throw new IllegalArgumentException("CO2Emitted cannot be less than zero");
        }

        if (this.equals(currentJourneySingleton)) {
            alertObservers();
        }
        this.CO2Emitted = CO2Emitted;
    }

    /**
     * Gets the max journey length
     * @return max journey length
     */
    public int getMaxJourneyLength() {
        return MAX_JOURNEY_LENGTH;
    }

    /**
     * Given a new stop to add to the journey, toAdd is added after the after stopBeforeNew
     * @param stopBeforeNew The stop just before the new stop. If Null the stop is added to the front of the list.
     * @param toAdd The new Location to add to the list of stops
     * @throws IllegalStateException Thrown if there are too many stops in the journey
     * @throws ArrayIndexOutOfBoundsException Thrown if the stopBeforeNew isn't within the journey
     */
    public void addStopAfterLocation(Location stopBeforeNew, Location toAdd) throws IllegalStateException, ArrayIndexOutOfBoundsException {
        if (stops.size() == MAX_JOURNEY_LENGTH) {
            throw new IllegalStateException("Too many stops");
        }

        if (stopBeforeNew == null) {
            stops.add(0, toAdd);
            if (this.equals(currentJourneySingleton)) {
                alertObservers();
            }
            return;
        }

        for (int i = 0; i < stops.size(); i++) {
            if (stopBeforeNew.equals(stops.get(i))) {
                stops.add(i + 1, toAdd);
                if (this.equals(currentJourneySingleton)) {
                    alertObservers();
                }
                return;
            }
        }

        throw new ArrayIndexOutOfBoundsException("The stop before the new stop doesn't exist in Journey");
    }

    /**
     * Given a location of a stop within the Journey, it removes it from the journey
     * @param toRemove The stop to remove
     * @throws ArrayIndexOutOfBoundsException Thrown if the stop isn't within the journey
     * @throws IllegalStateException Thrown when the length of the list implies that there is no start / end
     */
    public void removeStop(Location toRemove) throws ArrayIndexOutOfBoundsException, IllegalStateException {
        if (stops.size() == 2) {
            throw new IllegalStateException("Need at least two stops");
        }
        for (int i = 0; i < stops.size(); i++) {
            if (stops.get(i).equals(toRemove)) {
                stops.remove(i);
                return;
            }
        }
        throw new ArrayIndexOutOfBoundsException("Stop wasn't in Journey");
    }

    /**
     * Sets the stops of the journey to the locations provided
     * @param locations The locations to set as stops
     */
    public void setStops(ArrayList<Location> locations) {
        if (locations.size() > MAX_JOURNEY_LENGTH) {
            return;
        }
        stops = locations;

    }

    /**
     * Returns the Start of the destination.
     * @return Location for the start of the journey
     */
    public Location getStartLocation() {
        return this.stops.get(0);
    }


}
