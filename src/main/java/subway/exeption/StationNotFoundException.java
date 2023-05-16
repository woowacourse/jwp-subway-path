package subway.exeption;

public class StationNotFoundException extends SubwayException {
    public StationNotFoundException(final String message) {
        super(message);
    }
}
