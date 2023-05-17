package subway.global.common;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorResponse {

    private String message;
    private int statusCode;
    private List<FieldError> errorFields;

    private ErrorResponse() {
    }

    private ErrorResponse(String message, int statusCode, List<FieldError> errorFields) {
        this.message = message;
        this.statusCode = statusCode;
        this.errorFields = errorFields;
    }

    public ErrorResponse(final ErrorCode errorCode, final List<FieldError> errorFields) {
        this(errorCode.getMessage(), errorCode.getStatus().value(), errorFields);
    }

    public ErrorResponse(final ErrorCode errorCode) {
        this(errorCode.getMessage(), errorCode.getStatus().value(), null);
    }

    public ErrorResponse(final String message, final HttpStatus statusCode) {
        this(message, statusCode.value(), null);
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<FieldError> getErrorFields() {
        return errorFields;
    }
}
