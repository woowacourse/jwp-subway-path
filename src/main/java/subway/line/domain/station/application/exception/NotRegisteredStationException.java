package subway.line.domain.station.application.exception;

import subway.common.exception.ServerErrorException;

public class NotRegisteredStationException extends ServerErrorException {
    public static final String MESSAGE = "식별자가 등록되지 않은 역입니다.";

    public NotRegisteredStationException() {
        super(MESSAGE);
    }

    public NotRegisteredStationException(Throwable cause) {
        super(MESSAGE, cause);
    }

    protected NotRegisteredStationException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}
