package subway.exception;

public abstract class BaseException extends RuntimeException {

    public BaseException(final String message) {
        super(message);
    }

    public abstract BaseExceptionType exceptionType();
}
