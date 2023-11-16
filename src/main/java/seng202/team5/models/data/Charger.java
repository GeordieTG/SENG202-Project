package seng202.team5.models.data;

import java.util.ArrayList;

/**
 * This class holds all the data of a ChargingStation
 */
public class Charger {

    /**
     * The location of the object
     */
    private Location location = null;

    /**
     * Date the charging station was first operational
     */
    private String date = null;

    /**
     * Number of connectors
     */
    private int numberOfConnectors = 0;

    /**
     * Does it cost to charge at this station
     */
    private boolean hasChargingCost = false;

    /**
     * Either AC, DC or Mixed depending on the connectors that it has
     */
    private String powerType;

    /**
     * The current supplied by the station (AC/DC)
     */
    private String currentType = null;

    /**
     * The given name of the Charging Station
     */
    private String name = null;

    /**
     * The name of the operating company of the Charging Station
     */
    private String operator = null;

    /**
     * The name of the company that owns the Charging Station
     */
    private String owner = null;

    /**
     * The Physical address of the charging station
     */
    private String address = null;

    /**
     * Boolean for if the charger is open 24/7
     */
    private boolean alwaysOpen = true;

    /**
     * The amount of Car Parks for the Charging Station
     */
    private int carparkCount = -1;

    /**
     * Boolean for if it costs money to charge at the station
     */
    private boolean carparkNeedsPayment = false;

    /**
     * How long the car can be charged for at the station in minutes
     */
    private int maxTimeLimit = -1;

    /**
     * Boolean for if the charging station has nearby tourist attractions
     */
    private boolean touristAttraction = false;

    /**
     * Integer for the user's rating on a given charger
     */
    private int rating;

    /**
     * Contains the different Connectors that are available at the charger
     */
    private ArrayList<Connector> connectorList = new ArrayList<>();

    /**
     * Default constructor
     */
    public Charger(){}


    /**
     * Complete charger constructor
     * @param location location of the charger
     * @param name name of the charger
     * @param operator operator of the charger
     * @param owner owner of the charger
     * @param address address of the charger
     * @param alwaysOpen if the charger is always open
     * @param carParkCount number of car parks
     * @param carParkNeedsPayment if the user needs to pay to park
     * @param maxTimeLimit the maximum time limit of the charger
     * @param touristAttraction if there are any tourist attractions nearby
     * @throws IllegalArgumentException Exception thrown if any data is missing
     */
    public Charger(Location location, String name, String operator, String owner, String address, boolean alwaysOpen, int carParkCount, boolean carParkNeedsPayment, int maxTimeLimit, boolean touristAttraction, int rating) throws IllegalArgumentException{
        if (location != null) {
            this.location = location;
        } else {
            throw new IllegalArgumentException("Location is required");
        }
        if (name != null) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name is required");
        }
        this.operator = operator;
        this.owner = owner;
        this.address = address;
        this.alwaysOpen = alwaysOpen;
        this.carparkCount = carParkCount;
        this.carparkNeedsPayment = carParkNeedsPayment;
        this.maxTimeLimit = maxTimeLimit;
        this.touristAttraction = touristAttraction;
        this.rating = rating;
    }

// Getters

    /**
     * Gets the user rating
     * @return The user rating
     */
    public int getRating() {
        return rating;
    }



    /**
     * Gets the current type
     * @return The Type of Current at the charging station
     */
    public String getCurrentType() {
        return currentType;
    }

    /**
     * Gets the location
     * @return The Physical Location of the charging station
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the latitude
     * @return The latitude of the Charging station
     */
    public float getLatitude() {
        return location.getLatitude();
    }

    /**
     * Gets the longitude
     * @return The longitude of the Charging station
     */
    public float getLongitude() {
        return location.getLongitude();
    }

    /**
     * Gets the name
     * @return The name of the charging station
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the operator
     * @return The name of the operating company
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Gets the owner
     * @return The name of the company that owns the charger
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Gets the date
     * @return The date the charger was first operational
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the number of connectors
     * @return number of connectors
     */
    public int getNumberOfConnectors() {
        return numberOfConnectors;
    }

    /**
     * Gets the address
     * @return The Physical address of the charging station
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets if charger is always open
     * @return Boolean representing if the station is 24 hours
     */
    public boolean isAlwaysOpen() {
        return alwaysOpen;
    }

    /**
     * Gets the number of car parks
     * @return The amount of car parks at the charging station
     */
    public int getCarParkCount() {
        return carparkCount;
    }

    /**
     * Gets if the car park needs payment
     * @return Boolean representing if the charging station has a parking cost
     */
    public boolean isCarParkNeedsPayment() {
        return carparkNeedsPayment;
    }

    /**
     * Gets the max time limit
     * @return The max amount of time a car can stay at the carpark
     */
    public int getMaxTimeLimit() {
        return maxTimeLimit;
    }

    /**
     * Gets if there is a tourist attraction nearby
     * @return Boolean representing if the station has nearby tourist attractions
     */
    public boolean isTouristAttraction() {
        return touristAttraction;
    }

    /**
     * Gets a list of connectors
     * @return A list of all the connectors that are available at the charging station
     */
    public ArrayList<Connector> getConnectorList() {
        return connectorList;
    }


// Setters

    /**
     * Sets the user rating of a charger
     * @param rating The user rating of a charger
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Sets the powerType
     * @param powerType the powerType of the charger.
     */
    public void setPowerType(String powerType) {
        this.powerType = powerType;
    }



    /**
     * Sets the location
     * @param location The new location of the charger
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Sets the name
     * @param name The new name of the charger
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the operator
     * @param operator The new name of the operating company
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * Sets the owner
     * @param owner The new name of the company that owns the charger
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Sets the address
     * @param address the new physical address of the charger
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets if charger is always open
     * @param alwaysOpen New truth value for if the station is open
     */
    public void setAlwaysOpen(boolean alwaysOpen) {
        this.alwaysOpen = alwaysOpen;
    }

    /**
     * Sets car park count
     * @param carparkCount the new amount of car parks available
     */
    public void setCarparkCount(int carparkCount) {
        this.carparkCount = carparkCount;
    }

    /**
     * Sets whether car park needs payment
     * @param carparkNeedsPayment The new cost that it is to park
     */
    public void setCarparkNeedsPayment(boolean carparkNeedsPayment) {
        this.carparkNeedsPayment = carparkNeedsPayment;
    }

    /**
     * Sets max time limit
     * @param maxTimeLimit The new max time limit for the carpark
     */
    public void setMaxTimeLimit(int maxTimeLimit) {
        this.maxTimeLimit = maxTimeLimit;
    }

    /**
     * Sets whether there is a tourist attraction nearby
     * @param touristAttraction The new truth value for if there is a tourist attraction
     */
    public void setTouristAttraction(boolean touristAttraction) {
        this.touristAttraction = touristAttraction;
    }

    /**
     * Sets the list of connectors
     * @param connectorList The new list of connectors for the Charging station
     */
    public void setConnectorList(ArrayList<Connector> connectorList) {
        this.connectorList = connectorList;
    }

    /**
     * Adds a connector to the list of connectors.
     * If a connector of the same type is already in the list then the new connector's count is just
     * added to the list of connectors
     * @param connector The connector to connect
     */
    public void addConnector(Connector connector) {
        this.numberOfConnectors += connector.getCount();
        for (Connector listConnector : connectorList) {
            if (listConnector.sameType(connector)) {
                listConnector.setCount(connector.getCount() + listConnector.getCount());
                return;
            }
        }
        connectorList.add(connector);
    }

    /**
     * Removes all the Connectors of the given connector type from the Charger.
     * @param connector The connector type to remove
     * @throws  ArrayIndexOutOfBoundsException Thrown when the connector is not within the list
     */
    public void removeConnector(Connector connector) throws ArrayIndexOutOfBoundsException{
        for (int i = 0; i < connectorList.size(); i++) {
            if (connector.sameType(connectorList.get(i))) {
                numberOfConnectors -= connectorList.get(i).getCount();
                connectorList.remove(i);
                return;
            }
        }
        throw new ArrayIndexOutOfBoundsException("Connector is not within list");
    }

    /**
     * Removes count amount of connectors of the given connector type.
     * @param connector The connector to remove
     * @param count The amount of given connectors to remove
     */
    public void removeConnector(Connector connector, int count) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        for (int i = 0; i < connectorList.size(); i++) {
            if (connector.equals(connectorList.get(i))) {
                if (count > connectorList.get(i).getCount()) {
                    throw new IllegalArgumentException("Count is larger than the amount of chargers");
                } else if (count == connectorList.get(i).getCount()) {
                    numberOfConnectors -= connectorList.get(i).getCount();
                    connectorList.remove(i);
                    return;
                } else {
                    numberOfConnectors -= count;
                    connectorList.get(i).setCount(connectorList.get(i).getCount() - count);
                    return;
                }
            }
        }
        throw new ArrayIndexOutOfBoundsException("Connector Not Found");
    }

    /**
     * Returns boolean  of if the charger has a cost to use
     * @return if the charger has a charging cost
     */
    public boolean isHasChargingCost() {
        return hasChargingCost;
    }

    /**
     * Sets whether the charger has a cost to use
     * @param hasChargingCost The new boolean dictating if the charger has a cost to use
     */
    public void setHasChargingCost(boolean hasChargingCost) {
        this.hasChargingCost = hasChargingCost;
    }

    /**
     * Returns a textual representation of the charger for use in the map view
     * @return String representation of the charger
     */
    public String toMarkerString() {
        return String.format("%s %s", name, getAddress());
    }
}
