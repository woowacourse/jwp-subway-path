package subway.exception.custom;

public class LineDoesNotContainStationException extends RuntimeException {

    public LineDoesNotContainStationException(final String message) {
        super(message);
    }
}
