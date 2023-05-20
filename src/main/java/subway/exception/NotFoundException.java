package subway.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends SubwayException {
    public NotFoundException(final ErrorCode errorCode) {
        super(errorCode.getErrorMessage(), HttpStatus.NOT_FOUND);
    }
}
