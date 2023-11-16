package seng202.team5.models.data;

import java.util.Objects;

/**
 * This class is used for representing Connectors
 */
public class Connector {

    /**
     * The power type of  connector
     */
    private String powerType;

    /**
     * The socket of a connector
     */
    private String socket;

    /**
     * The status of a connector
     */
    private String status;

    /**
     * The wattage of a connector
     */
    private int wattage;

    /**
     * The number of this type of connector on the charger
     */
    private int count;

    /**
     * Default Constructor
     */
    public Connector() {}

    /**
     * Will make a connector from the given information in the string.
     * @param csvInputString input string.
     */
    public Connector(String csvInputString) throws IllegalArgumentException {
        String givenString = csvInputString.replace("{", "");
        givenString = givenString.replace("}", "");
        String[] connectorInformation = givenString.split(",");

        String powerType = connectorInformation[0];
        if (Objects.equals(powerType.toUpperCase(), "AC") ||
                Objects.equals(powerType.toUpperCase(), "DC") ||
                Objects.equals(powerType.toUpperCase(), "MIXED")) {
            this.setPowerType(powerType);
        } else {
            this.setPowerType(null);
        }

        int wattage = Integer.parseInt(((connectorInformation[1]).split(" "))[1]);
        this.setWattage(wattage);

        String socket = connectorInformation[2].trim();
        this.setSocket(socket);

        String status = ((connectorInformation[3]).split("Status: "))[1];
        this.setStatus(status);

        int count = Integer.parseInt(((connectorInformation[4]).split("Count:"))[1]);

        if (count >= 0) {
            this.setCount(count);
        } else {
            this.setCount(-1);
        }
    }

    /**
     * Will convert a connector into the string format required.
     * @param connector given connector.
     * @return returns the information of the connector in the required string format
     */
    public String toString(Connector connector) {
        String csvString = "{" +
                connector.getPowerType() +
                ", " + connector.getWattage() + " kW" +
                ", " + connector.getSocket() +
                ", Status: " + connector.getStatus() +
                ", Count:" + connector.getCount() +
                "}";
        return csvString;
    }

    /**
     * Initializes an instance of a Connector class
     *
     * @param powerType The power type of the connector
     * @param socket    The socket of the connector
     * @param wattage   The wattage of the connector
     * @param status    The status of the connector
     * @param count     The number of a specific type of connector on the charger
     * @throws IllegalArgumentException Thrown if the user tries to create a charger with less than 1 connector
     */
    public Connector(String powerType, String socket, int wattage, String status, int count) throws IllegalArgumentException {
        this.powerType = powerType;
        this.socket = socket;
        this.status = status;
        this.wattage = wattage;
        if (count > 0) {
            this.count = count;
        } else {
            throw new IllegalArgumentException("Count must be greater than 0");
        }
    }

    /**
     * Gets the power type of the connector
     * @return A string representing the power type of the connector
     */
    public String getPowerType() {
        return powerType;
    }

    /**
     * Gets the number of a specific type of connector on the charger
     * @return the number of this type of connector on the charger
     */
    public int getCount() {
        return count;
    }

    /**
     * Gets the socket of a given connector
     * @return A string representing the socket of a connector
     */
    public String getSocket() {
        return socket;
    }

    /**
     * Gets the status of a given connector
     * @return A string representing the status of a connector
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the wattage of a connector
     * @return An int representing the wattage of a connector
     */
    public int getWattage() {
        return wattage;
    }

    /**
     * Sets the power type of the connector
     * @param powerType The desired power type of the connector
     */
    public void setPowerType(String powerType) {
        this.powerType = powerType;
    }

    /**
     * Sets the socket of the connector
     * @param socket The desired socket of a connector
     */
    public void setSocket(String socket) {
        this.socket = socket;
    }

    /**
     * Sets the status of the connector
     * @param status The desired status of a connector
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the wattage of the connector
     * @param wattage The desired wattage of a connector
     */
    public void setWattage(int wattage) {
        this.wattage = wattage;
    }

    /**
     * Sets the number of a specific type of connector on the charger
     * @param count number of this type of connector on the charger
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Determines whether a given connector is the same as the instance
     * of a connector.
     * @param toCompare the connector to compare.
     * @return a boolean, true if the connectors are the same or false if
     *      they are not the same connector.
     */
    public Boolean sameType(Connector toCompare){
        return Objects.equals(this.powerType, toCompare.getPowerType()) &&
                Objects.equals(this.socket, toCompare.getSocket()) &&
                Objects.equals(this.status, toCompare.getStatus()) &&
                Objects.equals(this.wattage, toCompare.getWattage());
    }

    /**
     * Equals method for the connector. Doesn't require the count to be the same.
     * @param object The object to compare with
     * @return Boolean representing if they are the same connector
     */
    @Override
    public boolean equals(Object object) {
        try {
            Connector connector = (Connector) object;
            return this.sameType(connector);
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Returns the power type of a charger as a string
     * @return String of power type
     */
    @Override
    public String toString() {
        return this.powerType;
    }
}
