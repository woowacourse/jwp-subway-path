package subway.exception;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {

    private final HttpStatus httpStatus;

    public HttpException(final HttpStatus httpStatus, final String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
