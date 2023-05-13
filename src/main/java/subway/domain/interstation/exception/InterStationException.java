package subway.domain.interstation.exception;

import subway.exception.BusinessException;

public class InterStationException extends BusinessException {

    public InterStationException(final String message) {
        super(message);
    }
}
