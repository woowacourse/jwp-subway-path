package subway.exception;

import org.springframework.http.HttpStatus;

public final class InvalidSectionException extends HttpException {

    public InvalidSectionException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
