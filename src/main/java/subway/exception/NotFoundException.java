package subway.exception;

public class NotFoundException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String errorMessage;

    public NotFoundException(final ErrorCode errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
