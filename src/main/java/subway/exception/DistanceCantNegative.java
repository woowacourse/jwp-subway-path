package subway.exception;

import subway.error.exception.ErrorCode;
import subway.error.exception.SubwayException;

public class DistanceCantNegative extends SubwayException {

    public static SubwayException EXECUTE = new DistanceCantNegative();

    public DistanceCantNegative() {
        super(new ErrorCode(400, "거리는 기존 구간의 길이보다 작아야합니다."));
    }

}
