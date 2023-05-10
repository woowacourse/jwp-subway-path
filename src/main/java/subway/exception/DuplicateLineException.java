package subway.exception;

public class DuplicateLineException extends RuntimeException {

    private static final String MESSAGE = "%s 노선은 이미 존재합니다.";

    public DuplicateLineException(final String name) {
        super(String.format(MESSAGE, name));
    }
}
