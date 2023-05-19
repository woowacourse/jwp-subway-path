package subway.exception;

public class NotFoundException extends CustomException {
    public NotFoundException(final ErrorMessage errorMessage) {
        super(errorMessage.getErrorMessage());
    }
}
