package subway.line.domain.section.application.exception;

import subway.common.exception.InvalidUserInputException;

public class StationNotConnectedException extends InvalidUserInputException {
    public StationNotConnectedException() {
        super();
    }

    public StationNotConnectedException(String message) {
        super(message);
    }

    public StationNotConnectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public StationNotConnectedException(Throwable cause) {
        super(cause);
    }

    protected StationNotConnectedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
