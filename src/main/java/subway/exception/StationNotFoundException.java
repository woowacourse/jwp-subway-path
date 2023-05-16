package subway.exception;

import java.util.NoSuchElementException;

public class StationNotFoundException extends NoSuchElementException {

    public StationNotFoundException(String message) {
        super(message);
    }
}
