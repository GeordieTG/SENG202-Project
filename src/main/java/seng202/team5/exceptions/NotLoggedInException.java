package seng202.team5.exceptions;

/**
 * Custom exception for anywhere where there is
 * functionality locked behind a user needing to be logged in.
 */
public class NotLoggedInException extends Exception {

    /**
     * Simple constructor that passes to parent Exception class.
     * @param message error message
     */
    public NotLoggedInException(final String message) {
        super(message);
    }
}
