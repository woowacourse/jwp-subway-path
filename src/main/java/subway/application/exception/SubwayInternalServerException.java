package subway.application.exception;

public class SubwayInternalServerException extends RuntimeException {

    public SubwayInternalServerException(String message) {
        super(message);
    }
}
