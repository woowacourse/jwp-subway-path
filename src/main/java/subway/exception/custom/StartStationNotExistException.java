package subway.exception.custom;

public class StartStationNotExistException extends RuntimeException {

    public StartStationNotExistException(final String message) {
        super(message);
    }
}
