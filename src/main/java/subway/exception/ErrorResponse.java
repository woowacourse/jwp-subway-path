package subway.exception;

public class ErrorResponse {
    private final String error;

    private ErrorResponse(String error) {
        this.error = error;
    }

    public static ErrorResponse from(String message) {
        return new ErrorResponse(message);
    }

    public String getError() {
        return error;
    }
}
