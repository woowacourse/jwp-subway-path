package subway.domain.exception;

public class IllegalDistanceArgumentException extends ClientResponsibleException {

    public IllegalDistanceArgumentException(final String message) {
        super(message);
    }
}
