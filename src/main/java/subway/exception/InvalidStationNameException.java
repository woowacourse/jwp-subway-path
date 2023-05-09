package subway.exception;

import org.springframework.http.HttpStatus;

public final class InvalidStationNameException extends HttpException{

    public InvalidStationNameException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
