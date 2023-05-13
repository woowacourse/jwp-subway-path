package subway.domain.line.exception;

import subway.exception.BusinessException;

public class LineColorException extends BusinessException {

    public LineColorException(final String message) {
        super(message);
    }
}
