package seng202.team5.exceptions;

/**
 * Custom IncorrectConfigForEnvException to be thrown
 * if something goes wrong with the env file.
 */
public class IncorrectConfigForEnvException extends Exception {

    /**
     * Simple constructor that passes to parent Exception class.
     * @param message error message
     */
    public IncorrectConfigForEnvException(final String message) {
        super("Missing key for " + message);
    }
}
