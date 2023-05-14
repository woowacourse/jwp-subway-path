package subway.global.exception.line;

import org.springframework.http.HttpStatus;
import subway.global.exception.common.BusinessException;

public class CanNotDuplicatedLineNameException extends BusinessException {

    public CanNotDuplicatedLineNameException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
