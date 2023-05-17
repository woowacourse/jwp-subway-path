package subway.application.exception;

public class CircularRouteException extends ExpectedException {

    private static final String MESSAGE = "순환노선은 등록할 수 없습니다.";

    public CircularRouteException() {
        super(MESSAGE);
    }
}
