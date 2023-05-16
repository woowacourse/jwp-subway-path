package subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidFareException extends HttpException {
    public InvalidFareException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
