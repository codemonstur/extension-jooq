package extension.jooq;

/**
 * The {@code WrongTypeForField} class.
 */
public class WrongTypeForField extends Exception {

    /**
     * Creates a new instance.
     *
     * @param message the message
     */
    public WrongTypeForField(final String message) {
        super(message);
    }
    /**
     * Creates a new instance.
     *
     * @param message the message
     * @param cause the cause
     */
    public WrongTypeForField(final String message, final Throwable cause) {
        super(message, cause);
    }

}
