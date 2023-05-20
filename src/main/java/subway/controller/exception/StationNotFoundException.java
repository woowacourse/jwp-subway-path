package subway.controller.exception;

public class StationNotFoundException extends IllegalArgumentException {

    public StationNotFoundException(final String message) {
        super(message);
    }
}
