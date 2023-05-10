package subway.exception;

public class InvalidException extends CustomException {
    public InvalidException(final ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
    }
}
