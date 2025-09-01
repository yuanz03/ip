package shadowbuddy.services;

/**
 * Represents a Checked Exception for raising errors in Shadow chatbot.
 * The ShadowException class wraps an error message and is thrown when an error occurs in Shadow.
 */
public class ShadowException extends Exception {
    /**
     * Initializes a ShadowException instance with the given String message.
     *
     * @param message The error message describing the encountered exception.
     */
    public ShadowException(String message) {
        super(message);
    }
}
