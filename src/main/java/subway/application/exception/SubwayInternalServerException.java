package subway.application.exception;

public class SubwayInternalServerException extends RuntimeException {

    public SubwayInternalServerException(final String message) {
        super(message);
    }
}
