package subway.exception;

public class DuplicateStationException extends RuntimeException {

    private static final String MESSAGE = "%s 역은 이미 존재합니다.";

    public DuplicateStationException(final String name) {
        super(String.format(MESSAGE, name));
    }
}
