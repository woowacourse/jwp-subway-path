package subway.controller.exception;

public class InvalidSectionException extends IllegalArgumentException {

    public InvalidSectionException(final String message) {
        super(message);
    }
}
