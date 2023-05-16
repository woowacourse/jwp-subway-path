package subway.exception.not_found;

public class NotFoundException extends IllegalArgumentException {

    public NotFoundException(final String message) {
        super(message);
    }
}
