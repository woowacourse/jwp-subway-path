package subway.exception.bad_request;

public class BadRequestException extends IllegalArgumentException {

    public BadRequestException(final String message) {
        super(message);
    }
}
