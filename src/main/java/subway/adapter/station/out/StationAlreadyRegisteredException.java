package subway.adapter.station.out;

import subway.exception.BusinessException;

public class StationAlreadyRegisteredException extends BusinessException {

    public StationAlreadyRegisteredException() {
        super("이미 등록된 역입니다.");
    }
}
