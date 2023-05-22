package subway.exception.line;

import org.springframework.http.HttpStatus;
import subway.exception.common.BusinessException;

public class CanNotDuplicatedLineNameException extends BusinessException {

    public CanNotDuplicatedLineNameException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
