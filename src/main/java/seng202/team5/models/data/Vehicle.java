package seng202.team5.models.data;

import java.util.Objects;

/**
 *This class represents the vehicles that can be registered within the application.
 */
public class Vehicle {

    /**
     * The plate number of the vehicle
     */
    private String plateNumber = null;

    /**
     * The model of the vehicle
     */
    private String model = "default";

    /**
     * The type of fuel the vehicle can receive
     */
    private String fuelType = "default";

    /**
     * The year the vehicle was made
     */
    private int year = 0;

    /**
     * The range of the vehicle (km)
     */
    private int range = 0;

    /**
     * Default Vehicle constructor
     */
    public Vehicle(){}

    /**
     * Initializes an instance of a Vehicle class
     *
     * @param plateNumber The plate number of the vehicle
     * @param model The model of the vehicle
     * @param fuelType The type of fuel the vehicle can receive
     * @param year The year the vehicle was made
     */
    public Vehicle(String plateNumber, String model, String fuelType, int year) {

        this.plateNumber = plateNumber;
        this.model = model;
        this.fuelType = fuelType;
        this.year = year;
    }

    /**
     * Initializes an instance of a Vehicle class including range.
     * @param plateNumber of the vehicle.
     * @param model of the vehicle.
     * @param fuelType the vehicle can receiver.
     * @param year the vehicle was made.
     * @param range of the vehicle.
     */
    public Vehicle(String plateNumber, String model, String fuelType, int year, int range) {

        this.plateNumber = plateNumber;
        this.model = model;
        this.fuelType = fuelType;
        this.year = year;
        this.range = range;
    }

    /**
     * Gets the plate number of the vehicle
     * @return An integer representing the plate number
     */
    public String getPlateNumber() {
        return plateNumber;
    }

    /**
     * Gets the model of the vehicle
     * @return A string representing the model of the vehicle
     */
    public String getModel() {
        return model;
    }

    /**
     * Gets the fuel type of the vehicle
     * @return A string representing the fuel type of the vehicle
     */
    public String getFuelType() {
        return fuelType;
    }

    /**
     * Gets the year the vehicle was made
     * @return An integer representing the year the vehicle was made
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the range of the vehicle
     * @return An integer representing the range of the vehicle
     */
    public int getRange() {
        return range;
    }

    /**
     * Sets the plate number of the vehicle
     * @param plateNumber The desired plate number of the vehicle
     */
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    /**
     * Sets the model of the vehicle
     * @param model The desired model of the vehicle
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Sets the fuel type of the vehicle
     * @param fuelType The desired fuel type of the vehicle
     */
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    /**
     * Sets the year the vehicle was made
     * @param year The desired year the vehicle was made
     */
    public void setYear(int year) {
        this.year = year;
    }


    /**
     * Method to check if two different instances of vehicle are equal. Overides default .equal()
     * @param o object being compared to
     * @return true if equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Vehicle)) {
            return false;
        }

        Vehicle c = (Vehicle) o;

        return (Objects.equals(plateNumber, c.getPlateNumber()))
                && (model.equals(c.getModel()))
                && (fuelType.equals(c.getFuelType()))
                && (year == c.getYear())
                && (range == c.getRange());
    }
}
