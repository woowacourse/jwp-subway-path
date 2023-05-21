package subway.exceptions;

public class IllegalStationException extends RuntimeException {
    public IllegalStationException(final String message) {
        super(message);
    }
}
