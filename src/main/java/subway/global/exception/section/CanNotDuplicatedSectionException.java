package subway.global.exception.section;

import org.springframework.http.HttpStatus;
import subway.global.exception.common.BusinessException;

public class CanNotDuplicatedSectionException extends BusinessException {

    public CanNotDuplicatedSectionException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
