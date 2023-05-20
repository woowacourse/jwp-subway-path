package subway.controller.exception;

public class InvalidStationException extends IllegalArgumentException {

    public InvalidStationException(final String message) {
        super(message);
    }
}
