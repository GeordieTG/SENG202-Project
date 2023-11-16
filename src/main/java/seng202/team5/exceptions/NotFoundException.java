package seng202.team5.exceptions;

/**
 * Custom NotFoundException to be thrown if
 * content is not found in database.
 */
public class NotFoundException extends Exception {

    /**
     * Simple constructor that passes to parent Exception class.
     * @param message error message
     */
    public NotFoundException(final String message) {
        super(message);
    }
}
