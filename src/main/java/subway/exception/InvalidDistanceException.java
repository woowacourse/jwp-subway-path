package subway.exception;

public class InvalidDistanceException extends IllegalArgumentException {

    public InvalidDistanceException(String message) {
        super(message);
    }
}
