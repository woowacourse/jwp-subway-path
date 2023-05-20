package subway.line.exception;

import subway.error.exception.ErrorCode;
import subway.error.exception.SubwayException;

public class LineNotFoundException extends SubwayException {

    public static SubwayException THROW = new LineNotFoundException();

    public LineNotFoundException() {
        super(new ErrorCode(404, "호선을 찾을 수 없습니다. 알맞은 값인지 확인해주세요!"));
    }

}
