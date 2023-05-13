package subway.exception.interstation;

import subway.exception.BusinessException;

public class DistanceException extends BusinessException {

    public DistanceException(final String message) {
        super(message);
    }
}
