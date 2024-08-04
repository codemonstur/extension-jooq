package extension.jooq;

public class WrongTypeForField extends Exception {

    public WrongTypeForField(final String message) {
        super(message);
    }
    public WrongTypeForField(final String message, final Throwable cause) {
        super(message, cause);
    }

}
