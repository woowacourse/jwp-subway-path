package subway.application.line.port.in;

import subway.exception.BusinessException;

public class LineNotFoundException extends BusinessException {

    public LineNotFoundException() {
        super("노선을 찾을 수 없습니다.");
    }
}
