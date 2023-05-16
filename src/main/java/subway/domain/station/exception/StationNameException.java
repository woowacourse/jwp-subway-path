package subway.domain.station.exception;

import subway.exception.BusinessException;

public class StationNameException extends BusinessException {

    public StationNameException(final String message) {
        super(message);
    }
}
