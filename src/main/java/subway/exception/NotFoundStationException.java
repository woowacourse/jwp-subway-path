package subway.exception;

public class NotFoundStationException extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 역입니다.";

    public NotFoundStationException() {
        super(MESSAGE);
    }
}
