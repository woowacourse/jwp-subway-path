package subway.exception;

import java.util.NoSuchElementException;

public class LineNotFoundException extends NoSuchElementException {

    public LineNotFoundException(String message) {
        super(message);
    }
}
