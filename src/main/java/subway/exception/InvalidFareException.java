package subway.exception;

public class InvalidFareException extends IllegalArgumentException {

    public InvalidFareException(String message) {
        super(message);
    }
}
