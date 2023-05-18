package subway.controller.exception;

public class BusinessException extends IllegalArgumentException {

    public BusinessException(final String message) {
        super(message);
    }
}
