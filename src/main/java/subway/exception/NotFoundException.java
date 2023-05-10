package subway.exception;

public class NotFoundException extends CustomException {
    public NotFoundException(final ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
    }
}
