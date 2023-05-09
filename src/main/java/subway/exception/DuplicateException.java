package subway.exception;

public class DuplicateException extends CustomException {
    public DuplicateException(final ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
    }
}
