package subway.exception.line;

import static subway.exception.line.LineExceptionType.ABNORMAL_EXCEPTION;

import subway.exception.BaseException;
import subway.exception.BaseExceptionType;

public class LineException extends BaseException {

    private final LineExceptionType exceptionType;

    public LineException(final LineExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    public LineException(final String message) {
        super(String.format(ABNORMAL_EXCEPTION.errorMessage(), message));
        this.exceptionType = ABNORMAL_EXCEPTION;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
