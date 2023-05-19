package subway.exception;

public class LineNotFoundException extends IllegalArgumentException {

    public LineNotFoundException(String message) {
        super(message);
    }

}
