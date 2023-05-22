package subway.line.application.exception;

import subway.common.exception.ServerErrorException;

public class NotRegisteredLineException extends ServerErrorException {
    public static final String MESSAGE = "식별자가 등록되지 않은 노선입니다.";

    public NotRegisteredLineException() {
        super(MESSAGE);
    }

    public NotRegisteredLineException(Throwable cause) {
        super(MESSAGE, cause);
    }

    protected NotRegisteredLineException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}
