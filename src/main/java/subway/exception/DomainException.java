package subway.exception;

public class DomainException extends RuntimeException {
    private final ExceptionType exceptionType;

    public DomainException(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    @Override
    public String getMessage() {
        return exceptionType.name();
    }
}
