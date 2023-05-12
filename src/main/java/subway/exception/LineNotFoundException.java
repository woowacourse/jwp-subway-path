package subway.exception;

public class LineNotFoundException extends RuntimeException {

    public LineNotFoundException(final String message) {
        super(message);
    }
}
