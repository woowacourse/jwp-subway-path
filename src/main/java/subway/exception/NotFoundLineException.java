package subway.exception;

public class NotFoundLineException extends RuntimeException {

    private static final String MESSAGE = "%s 노선은 존재하지 않습니다.";
    private static final String MESSAGE_ID = "%d번 노선은 존재하지 않습니다.";

    public NotFoundLineException(final Long id) {
        super(String.format(MESSAGE_ID, id));
    }

    public NotFoundLineException(final String name) {
        super(String.format(MESSAGE, name));
    }
}
