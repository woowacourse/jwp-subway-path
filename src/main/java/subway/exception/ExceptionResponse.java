package subway.exception;

public class ExceptionResponse {

    private String message;

    private ExceptionResponse() {
    }

    public ExceptionResponse(final String message) {
        this.message = message;
    }

    public static ExceptionResponse of(final String message) {
        return new ExceptionResponse(message);
    }

    public String getMessage() {
        return message;
    }
}
