package subway.dto.exception;

public class ExceptionResponse {

    private final String message;
    private final int code;

    private ExceptionResponse(final String message, final int code) {
        this.message = message;
        this.code = code;
    }

    public static ExceptionResponse from(final String message, final int code) {
        return new ExceptionResponse(message, code);
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
