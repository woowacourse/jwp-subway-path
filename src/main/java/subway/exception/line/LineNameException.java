package subway.exception.line;

import subway.exception.BusinessException;

public class LineNameException extends BusinessException {

    public LineNameException(String message) {
        super(message);
    }
}
