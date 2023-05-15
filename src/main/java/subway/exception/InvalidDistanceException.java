package subway.exception;

import org.springframework.http.HttpStatus;

public final class InvalidDistanceException extends HttpException {

    public InvalidDistanceException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
