package subway.exception;

public class GlobalException extends RuntimeException {

    private final ErrorCode errorCode;

    public GlobalException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
