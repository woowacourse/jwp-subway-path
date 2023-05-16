package subway.domain.line.exception;

import subway.exception.BusinessException;

public class LineNameException extends BusinessException {

    public LineNameException(final String message) {
        super(message);
    }
}
