package subway.application.exception;

public class NoSuchStationException extends RuntimeException {
    public NoSuchStationException(final String message) {
        super(message);
    }
}

