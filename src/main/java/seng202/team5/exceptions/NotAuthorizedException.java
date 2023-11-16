package seng202.team5.exceptions;

/**
 * Custom Not AuthorisedException to be thrown if a
 * user provides incorrect credentials.
 * Or tries to access content they are not authorised to.
 */
public class NotAuthorizedException extends Exception {

    /**
     * Simple constructor that passes to parent Exception class.
     * @param message error message
     */
    public NotAuthorizedException(final String message) {
        super(message);
    }
}
