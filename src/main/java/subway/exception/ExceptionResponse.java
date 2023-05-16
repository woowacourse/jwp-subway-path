package subway.exception;

public class ExceptionResponse {

    private String message;

    ExceptionResponse() {

    }

    public ExceptionResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
