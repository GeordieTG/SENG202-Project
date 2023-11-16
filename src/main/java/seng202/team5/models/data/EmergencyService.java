package seng202.team5.models.data;

/**
 * Class to store the information of an Emergency Service
 */
public class EmergencyService {

    /**
     * Name of the service
     */
    private final String name;
    /**
     * Latitude of the service
     */
    private final float lat;
    /**
     * Longitude of the service
     */
    private final float lng;

    /**
     * Emergency Service constructor.
     * @param name - name of the emergency service.
     * @param lat - latitude of the emergency service.
     * @param lng - longitude of the emergency service.
     */
    public EmergencyService(String name, float lat, float lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * Gets the name of the emergency service.
     * @return returns the name of the emergency service.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the latitude of the emergency service.
     * @return returns the latitude of the emergency service.
     */
    public float getLat() {
        return lat;
    }

    /**
     * Gets the longitude of the emergency service.
     * @return returns the longitude of the emergency service.
     */
    public float getLng() {
        return lng;
    }

    /**
     * Checking if two EmergencyServices are the same or not.
     * @param o the object being compared to.
     * @return true or false.
     */
    @Override
    public boolean equals(Object o) {

        if (!(o instanceof EmergencyService)) {
            return false;
        }

        EmergencyService c = (EmergencyService) o;

        return (name.equals(c.getName()) &&
                lat == c.getLat() &&
                lng == c.getLng());
    }
}
