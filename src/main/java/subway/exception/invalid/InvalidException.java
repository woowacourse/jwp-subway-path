package subway.exception.invalid;

public class InvalidException extends RuntimeException {

    public InvalidException(final String message) {
        super(message);
    }

    public InvalidException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
