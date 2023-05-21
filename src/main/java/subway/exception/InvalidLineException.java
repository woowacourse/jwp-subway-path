package subway.exception;

import org.springframework.http.HttpStatus;

public final class InvalidLineException extends HttpException {

    public InvalidLineException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
