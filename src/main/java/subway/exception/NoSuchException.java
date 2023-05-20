package subway.exception;

import org.springframework.http.HttpStatus;

public class NoSuchException extends SubwayException {
    public NoSuchException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }
}
