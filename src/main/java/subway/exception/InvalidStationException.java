package subway.exception;

import org.springframework.http.HttpStatus;

public final class InvalidStationException extends HttpException {

    public InvalidStationException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
