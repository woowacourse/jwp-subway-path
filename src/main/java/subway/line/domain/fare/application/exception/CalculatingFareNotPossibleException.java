package subway.line.domain.fare.application.exception;

import subway.common.exception.BadRequestException;

public class CalculatingFareNotPossibleException extends BadRequestException {
    private static final String MESSAGE = "주어진 정보로 요금을 계산할 수 없습니다.";

    public CalculatingFareNotPossibleException() {
        super(MESSAGE);
    }

    public CalculatingFareNotPossibleException(Throwable cause) {
        super(MESSAGE, cause);
    }

    protected CalculatingFareNotPossibleException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}
