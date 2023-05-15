package subway.line.exception.line;

import subway.common.exception.BaseException;
import subway.common.exception.BaseExceptionType;

public class LineException extends BaseException {

    private final LineExceptionType exceptionType;

    public LineException(final LineExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    public LineException(final String message) {
        super(String.format(LineExceptionType.ABNORMAL_EXCEPTION.errorMessage(), message));
        this.exceptionType = LineExceptionType.ABNORMAL_EXCEPTION;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
