package subway.exception;

public class LineNotFoundException extends RuntimeException {

    private static final String MESSAGE_FOR_CLIENT = "라인을 찾을 수 없습니다.";

    public LineNotFoundException() {
        super(MESSAGE_FOR_CLIENT);
    }

    public LineNotFoundException(final String message) {
        super(message);
    }
}
