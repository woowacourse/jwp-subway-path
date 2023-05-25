package subway.line.domain.section.domain.exception;

import subway.common.exception.BadRequestException;

public class InvalidDistanceException extends BadRequestException {

    public static final String MESSAGE = "거리 정보는 양의 정수로 제한합니다.";

    public InvalidDistanceException() {
        super(MESSAGE);
    }

    public InvalidDistanceException(Throwable cause) {
        super(MESSAGE, cause);
    }

    protected InvalidDistanceException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}
