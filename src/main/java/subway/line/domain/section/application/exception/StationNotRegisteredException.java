package subway.line.domain.section.application.exception;

public class StationNotRegisteredException extends RuntimeException {
    public StationNotRegisteredException() {
        super();
    }

    public StationNotRegisteredException(String message) {
        super(message);
    }

    public StationNotRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public StationNotRegisteredException(Throwable cause) {
        super(cause);
    }

    protected StationNotRegisteredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
