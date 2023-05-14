package subway.common.exception;

public class ApiException extends RuntimeException {

    public ApiException(final String message) {
        super(message);
    }
}
