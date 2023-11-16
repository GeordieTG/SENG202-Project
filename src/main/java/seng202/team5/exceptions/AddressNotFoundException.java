package seng202.team5.exceptions;

/**
 * Custom AddressNotFoundException to be thrown
 * if there is an error on the address found
 */
public class AddressNotFoundException extends Exception {

    /**
     * Simple constructor that passes to parent Exception class.
     * @param message error message
     */
    public AddressNotFoundException(final String message) {
        super(message);
    }
}