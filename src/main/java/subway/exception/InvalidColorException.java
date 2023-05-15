package subway.exception;

import org.springframework.http.HttpStatus;

public final class InvalidColorException extends HttpException {

    public InvalidColorException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
