package seng202.team5.models.data;

/**
 * This class is used for representing the geographical location of objects.
 */
public class Location {

    /**
     * The latitude of the object.
     */
    private float latitude;

    /**
     * The longitude of the object.
     */
    private float longitude;

    /**
     * Base constructor of the Location class.
     * @param latitude The latitude position of the object.
     * @param longitude The longitude position of the object.
     */
    public Location(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Creates a location based off a string representation of a string.
     * @param locationString "latitude,longitude".
     */
    public Location(String locationString) {
        String[] values = locationString.split(",");
        this.latitude = Float.parseFloat(values[0]);
        this.longitude = Float.parseFloat(values[1]);
    }

    /**
     * Create a string representation of the location
     * @return String representing the location
     */
    public String toString() {
        return latitude + "," + longitude;
    }

    /**
     * Gets the latitude of the charger
     * @return The Latitude of the object
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude of the charger
     * @return The Longitude of the object
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Sets the latitude of the charger
     * @param latitude The new latitude of the object
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * Set the longitude of the charger
     * @param longitude The new longitude of the object
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * Checks if latitude and longitude are already in the database
     * @param o object being compared to
     * @return true if equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        Location c = (Location) o;

        return (latitude == c.getLatitude())
                && (longitude == c.getLongitude());
    }
}

