package subway.global.exception.line;

import org.springframework.http.HttpStatus;
import subway.global.exception.common.BusinessException;

public class CanNotFoundLineException extends BusinessException {

    public CanNotFoundLineException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
