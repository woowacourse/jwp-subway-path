package subway.exception;

public class StationNotFoundException extends IllegalArgumentException {

    public StationNotFoundException(String message) {
        super(message);
    }

}
