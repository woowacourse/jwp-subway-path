package subway.line.exception.station;

import subway.common.exception.BaseException;
import subway.common.exception.BaseExceptionType;

public class StationException extends BaseException {

    private final StationExceptionType exceptionType;

    public StationException(final StationExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}

