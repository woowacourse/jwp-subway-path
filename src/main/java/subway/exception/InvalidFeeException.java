package subway.exception;

public class InvalidFeeException extends IllegalArgumentException {

    public InvalidFeeException(String message) {
        super(message);
    }
}
