package subway.line.exception;

public class DuplicateLineNameException extends RuntimeException {

    public DuplicateLineNameException(String message) {
        super(message);
    }
}
