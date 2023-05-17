package subway.exception;

public class DuplicateException extends CustomException {
    public DuplicateException(final ErrorMessage errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
