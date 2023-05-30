package subway.ui.dto.response;

public class ExceptionResponse {

    private final String message;

    private ExceptionResponse(final String message) {
        this.message = message;
    }

    public static ExceptionResponse from(final String message) {
        return new ExceptionResponse(message);
    }

    public String getMessage() {
        return message;
    }
}
