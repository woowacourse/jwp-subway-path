package subway.line.application.exception;

import subway.common.exception.BadRequestException;

public class InvalidSectionColorException extends BadRequestException {
    private static final String MESSAGE = "잘못된 노선 색상입니다.";

    public InvalidSectionColorException() {
        super(MESSAGE);
    }

    public InvalidSectionColorException(Throwable cause) {
        super(MESSAGE, cause);
    }

    protected InvalidSectionColorException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}
