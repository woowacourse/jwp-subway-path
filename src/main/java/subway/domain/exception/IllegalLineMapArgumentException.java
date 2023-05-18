package subway.domain.exception;

public class IllegalLineMapArgumentException extends ClientResponsibleException {

    public IllegalLineMapArgumentException(final String message) {
        super(message);
    }
}
