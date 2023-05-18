package subway.line.domain.section.domain.exception;

import subway.common.exception.InvalidUserInputException;

public class InvalidDistanceException extends InvalidUserInputException {
    public InvalidDistanceException() {
        super();
    }

    public InvalidDistanceException(String message) {
        super(message);
    }

    public InvalidDistanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDistanceException(Throwable cause) {
        super(cause);
    }

    protected InvalidDistanceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
