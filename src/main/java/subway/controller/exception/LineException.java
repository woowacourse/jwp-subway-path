package subway.controller.exception;

public class LineException extends IllegalArgumentException {

    public LineException(final String message) {
        super(message);
    }
}
