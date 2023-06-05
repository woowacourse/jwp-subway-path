package subway.station.application.exception;

import subway.common.exception.BusinessException;

public class StationNotFoundException extends BusinessException {

    public StationNotFoundException() {
        super("존재하지 않는 역입니다.");
    }
}
