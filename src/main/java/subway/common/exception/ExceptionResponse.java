package subway.common.exception;

public class ExceptionResponse {

    private final String code;
    private final String message;

    public ExceptionResponse(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public static ExceptionResponse from(final BaseException e) {
        final BaseExceptionType type = e.exceptionType();
        return new ExceptionResponse(String.valueOf(type.errorCode()), type.errorMessage());
    }

    public static ExceptionResponse internalServerError(final String code) {
        return new ExceptionResponse(code, "예상하지 못한 서버의 예외 발생!");
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
