package subway.application.station.port.in;

import subway.exception.BusinessException;

public class StationNotFoundException extends BusinessException {

    public StationNotFoundException() {
        super("존재하지 않는 역입니다.");
    }
}
