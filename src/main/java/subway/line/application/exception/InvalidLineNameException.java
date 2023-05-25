package subway.line.application.exception;

import subway.common.exception.BadRequestException;

public class InvalidLineNameException extends BadRequestException {
    private static final String MESSAGE = "잘못된 노선 이름입니다.";

    public InvalidLineNameException() {
        super(InvalidLineNameException.MESSAGE);
    }

    public InvalidLineNameException(Throwable cause) {
        super(InvalidLineNameException.MESSAGE);
    }

    protected InvalidLineNameException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(InvalidLineNameException.MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}
