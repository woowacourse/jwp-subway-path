package subway.exception;

public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException() {
    }

    public StationNotFoundException(final String message) {
        super(message);
    }
}
