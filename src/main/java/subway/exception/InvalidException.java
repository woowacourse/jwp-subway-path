package subway.exception;

import org.springframework.http.HttpStatus;

public class InvalidException extends SubwayException {
    public InvalidException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }
}
