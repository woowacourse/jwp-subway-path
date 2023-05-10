package subway.exception.line;

import subway.exception.BaseException;
import subway.exception.BaseExceptionType;

public class LineException extends BaseException {

    private final LineExceptionType exceptionType;

    public LineException(final LineExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
