package seng202.team5.exceptions;

/**
 * Custom NotEnteredException to be thrown if a user couldn't
 * be saved into the CSV file.
 */
public class NotEnteredException extends Exception {

    /**
     * Simple constructor that passes to parent Exception class.
     * @param message error message
     */
    public NotEnteredException(final String message) {
        super(message);
    }
}
