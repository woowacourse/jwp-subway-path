package subway.line.domain.interstation.exception;

import subway.common.exception.BusinessException;

public class DistanceException extends BusinessException {

    public DistanceException(String message) {
        super(message);
    }
}
