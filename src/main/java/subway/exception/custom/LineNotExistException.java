package subway.exception.custom;

public class LineNotExistException extends RuntimeException {

    public LineNotExistException(final String message) {
        super(message);
    }
}
