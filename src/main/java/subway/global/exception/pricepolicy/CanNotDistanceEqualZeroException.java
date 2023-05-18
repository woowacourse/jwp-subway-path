package subway.global.exception.pricepolicy;

import org.springframework.http.HttpStatus;
import subway.global.exception.common.BusinessException;

public class CanNotDistanceEqualZeroException extends BusinessException {

    public CanNotDistanceEqualZeroException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
