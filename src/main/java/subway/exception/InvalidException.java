package subway.exception;

public class InvalidException extends CustomException {
    public InvalidException(final ErrorMessage errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
