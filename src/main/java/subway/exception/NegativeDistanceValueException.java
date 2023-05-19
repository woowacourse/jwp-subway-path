package subway.exception;

public class NegativeDistanceValueException extends SubwayException {
    public NegativeDistanceValueException(final int value) {
        super("거리는 음수가 될 수 없습니다 (입력값: " + value + ")");
    }

}
