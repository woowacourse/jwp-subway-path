package subway.station.domain.exception;

import subway.common.exception.BusinessException;

public class StationNameException extends BusinessException {

    public StationNameException(String message) {
        super(message);
    }
}
