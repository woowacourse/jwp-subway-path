package subway.exception;

public class DatabaseException extends RuntimeException{
    private final ExceptionType exceptionType;

    public DatabaseException(ExceptionType exceptionType) {
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
