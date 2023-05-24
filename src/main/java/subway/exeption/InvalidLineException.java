package subway.exeption;

public class InvalidLineException extends SubwayException {
    public InvalidLineException(final String message) {
        super(message);
    }
}
