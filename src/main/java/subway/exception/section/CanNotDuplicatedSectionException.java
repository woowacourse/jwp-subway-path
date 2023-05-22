package subway.exception.section;

import org.springframework.http.HttpStatus;
import subway.exception.common.BusinessException;

public class CanNotDuplicatedSectionException extends BusinessException {

    public CanNotDuplicatedSectionException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
