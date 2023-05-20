package subway.section.exception;

import subway.error.exception.ErrorCode;
import subway.error.exception.SubwayException;

public class SectionNotFoundException extends SubwayException {

    public static SubwayException THROW = new SectionNotFoundException();

    public SectionNotFoundException() {
        super(new ErrorCode(404, "구간을 찾을 수 없습니다. 알맞은 값인지 확인해주세요!"));
    }

}
