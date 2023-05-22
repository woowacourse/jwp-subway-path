package subway.route.exception;

public class IdenticalStationsException extends RuntimeException {

    public IdenticalStationsException(String message) {
        super(message);
    }
}
