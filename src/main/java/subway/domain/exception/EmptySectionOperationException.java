package subway.domain.exception;

public class EmptySectionOperationException extends RuntimeException {

    public EmptySectionOperationException(final String message) {
        super(message);
    }
}
