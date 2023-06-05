package subway.interstation.domain.exception;

import subway.common.exception.BusinessException;

public class DistanceException extends BusinessException {

    public DistanceException(String message) {
        super(message);
    }
}
