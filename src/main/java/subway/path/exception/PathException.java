package subway.path.exception;

import subway.common.exception.BaseException;
import subway.common.exception.BaseExceptionType;

public class PathException extends BaseException {

    private final PathExceptionType exceptionType;

    public PathException(final PathExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    public PathException(final String message) {
        super(String.format(PathExceptionType.ABNORMAL_EXCEPTION.errorMessage(), message));
        this.exceptionType = PathExceptionType.ABNORMAL_EXCEPTION;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}

