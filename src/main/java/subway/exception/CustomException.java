package subway.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {

    private final HttpStatus httpStatus;

    public CustomException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
