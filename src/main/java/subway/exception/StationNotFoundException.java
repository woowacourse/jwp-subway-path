package subway.exception;

import subway.global.error.exception.ErrorCode;
import subway.global.error.exception.SubwayException;

public class StationNotFoundException extends SubwayException {

    public static SubwayException THROW = new SectionNotFoundException();

    public StationNotFoundException() {
        super(new ErrorCode(404, "역을 찾을 수 없습니다. 알맞은 값인지 확인해주세요!"));
    }

}
