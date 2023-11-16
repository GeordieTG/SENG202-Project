package seng202.team5.models.databaseInteraction;

/**
 * Class that stores a filter object that can be used with the database
 */

public class Filter {

    /**
     * String representing the field to be filtered
     */

    private final String column;
    /**
     * String representing the filter operation
     */
    private final String operator;
    /**
     * String representing the value to be filtered by
     */
    private final String value;

    /**
     * Complete Filter constructor
     * @param column  The field to be filtered
     * @param operator The filter operation
     * @param value The value to be filtered by
     */
    public Filter(String column, String operator, String value) {

        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Gets the field to be filtered
     * @return String representing the field to be filtered
     */
    public String getColumn() {
        return column;
    }

    /**
     * Gets the value to be filtered by
     * @return String representing the value to be filtered by
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the field to be filtered
     * @param column The new field to be filtered
     */
    public void setColumn(String column) {
        column = this.column;
    }

    /**
     * Sets the value
     * @param value The new value to be filtered by
     */
    public void setValue(String value) {
        value = this.value;
    }

    /**
     * Sets the operator
     * @return String representing the operation of the filter
     */
    public String getOperator() {
        return operator;
    }
}
