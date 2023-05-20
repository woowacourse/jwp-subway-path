package subway.controller.exception;

public class LineNotFoundException extends IllegalArgumentException {

    public LineNotFoundException(final String message) {
        super(message);
    }
}
