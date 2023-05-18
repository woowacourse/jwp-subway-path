package subway.application.core.exception;

public class DistanceNotPositiveException extends ExpectedException {

    private static final String MESSAGE = "거리는 음수일 수 없습니다.";

    public DistanceNotPositiveException() {
        super(MESSAGE);
    }
}
