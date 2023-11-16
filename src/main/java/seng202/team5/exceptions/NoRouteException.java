package seng202.team5.exceptions;

/**
 * The No route exception is used whenever something
 * attempts to access the route saved in the JavaScriptBridge.
 * when there is no route to see.
 */
public class NoRouteException extends Exception {

    /**
     * Simple constructor that passes to parent Exception class.
     * @param message The message to be sent with the error
     */
    public NoRouteException(final String message) {
        super(message);
    }
}
