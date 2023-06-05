package subway.interstation.domain.exception;

import subway.common.exception.BusinessException;

public class InterStationException extends BusinessException {

    public InterStationException(String message) {
        super(message);
    }
}
