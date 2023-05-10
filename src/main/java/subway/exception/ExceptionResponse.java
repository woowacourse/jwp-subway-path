package subway.exception;

public class ExceptionResponse {

    private final String code;
    private final String message;

    public ExceptionResponse(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
