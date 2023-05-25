package subway.line.application.exception;

import subway.common.exception.BadRequestException;

public class LineNotHavingSectionException extends BadRequestException {
    private static final String MESSAGE = "해당 노선 위에 존재하지 않는 구간입니다.";

    public LineNotHavingSectionException() {
        super(LineNotHavingSectionException.MESSAGE);
    }

    public LineNotHavingSectionException(Throwable cause) {
        super(MESSAGE);
    }

    protected LineNotHavingSectionException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}
