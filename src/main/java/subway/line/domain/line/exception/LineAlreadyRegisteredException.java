package subway.line.domain.line.exception;

import subway.common.exception.BusinessException;

public class LineAlreadyRegisteredException extends BusinessException {

    public LineAlreadyRegisteredException() {
        super("이미 등록된 노선입니다.");
    }
}
