package subway.exception;

import org.springframework.http.HttpStatus;

public final class InvalidLineNameException extends HttpException {

    public InvalidLineNameException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
