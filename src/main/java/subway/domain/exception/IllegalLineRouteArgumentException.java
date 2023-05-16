package subway.domain.exception;

public class IllegalLineRouteArgumentException extends SubwayResponsibleException {

    public IllegalLineRouteArgumentException(final String message) {
        super(message);
    }
}
