package subway.exception.custom;

public class StationNotExistException extends RuntimeException {

    public StationNotExistException(final String message) {
        super(message);
    }
}
