package subway.exception;

public class NotFoundStationException extends RuntimeException {

    private static final String MESSAGE = "%s 역은 존재하지 않는 역입니다.";

    public NotFoundStationException(final String name) {
        super(String.format(MESSAGE, name));
    }
}
