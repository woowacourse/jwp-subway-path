package subway.line.domain.line.exception;

import subway.common.exception.BusinessException;

public class LineNameException extends BusinessException {

    public LineNameException(String message) {
        super(message);
    }
}
