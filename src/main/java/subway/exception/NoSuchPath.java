package subway.exception;

import org.springframework.http.HttpStatus;

public class NoSuchPath extends SubwayException {
    public NoSuchPath(ErrorCode errorcode) {
        super(errorcode.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }
}
