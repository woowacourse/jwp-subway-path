package subway.domain.exception;

public class RequestDataNotFoundException extends ClientResponsibleException {

    public RequestDataNotFoundException(final String message) {
        super(message);
    }
}
