package seng202.team5.exceptions;

/**
 * Custom DuplicateEntryException to be thrown if a db record creation is
 * attempted with a duplicate key.
 */
public class DuplicateEntryException extends Exception {

    /**
     * Simple constructor that passes to parent Exception class.
     * @param message error message
     */
    public DuplicateEntryException(final String message) {
        super(message);
    }
}
