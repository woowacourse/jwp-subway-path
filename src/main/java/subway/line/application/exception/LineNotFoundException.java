package subway.line.application.exception;

import subway.common.exception.BusinessException;

public class LineNotFoundException extends BusinessException {

    public LineNotFoundException() {
        super("노선을 찾을 수 없습니다.");
    }
}
