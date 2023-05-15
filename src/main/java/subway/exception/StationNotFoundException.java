package subway.exception;

import subway.error.exception.ErrorCode;
import subway.error.exception.SubwayException;

public class StationNotFoundException extends SubwayException {

    public static SubwayException THROW = new SectionNotFoundException();

    public StationNotFoundException() {
        super(new ErrorCode(404, "역을 찾을 수 없습니다."));
    }

}
