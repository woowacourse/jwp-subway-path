package subway.dto;

public class ErrorResponse {
    private final String errorCode;
    private final String reason;

    public ErrorResponse(String errorCode, String reason) {
        this.errorCode = errorCode;
        this.reason = reason;
    }

    public ErrorResponse(String errorCode) {
        this(errorCode, null);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getReason() {
        return reason;
    }
}
