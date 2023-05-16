package subway.domain.interstation.exception;

import subway.exception.BusinessException;

public class InterStationsException extends BusinessException {

    public InterStationsException(final String message) {
        super(message);
    }
}
