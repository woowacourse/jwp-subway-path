package subway.line.application.exception;

import subway.common.exception.ServerErrorException;

public class WrongStrategyMappedException extends ServerErrorException {
    private static final String MESSAGE = "조건에 부합하지 않는 전략이 매핑되었습니다.";

    public WrongStrategyMappedException() {
        super(MESSAGE);
    }

    public WrongStrategyMappedException(Throwable cause) {
        super(MESSAGE, cause);
    }

    protected WrongStrategyMappedException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}
