package subway.exception;

import org.springframework.http.HttpStatus;

public class DuplicateException extends SubwayException {
    public DuplicateException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }
}
