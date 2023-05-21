package subway.exception;

import org.springframework.http.HttpStatus;

public final class InvalidAgeException extends HttpException {

    public InvalidAgeException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
