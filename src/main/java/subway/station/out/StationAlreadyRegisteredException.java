package subway.station.out;

import subway.common.exception.BusinessException;

public class StationAlreadyRegisteredException extends BusinessException {

    public StationAlreadyRegisteredException() {
        super("이미 등록된 역입니다.");
    }
}
