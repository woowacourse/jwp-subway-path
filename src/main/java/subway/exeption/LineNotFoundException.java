package subway.exeption;

public class LineNotFoundException extends SubwayException {
    public LineNotFoundException(final String message) {
        super(message);
    }
}
