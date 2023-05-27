package subway.exception.section;

public class NegativeDistanceValueException extends SectionException {
    public NegativeDistanceValueException(final int value) {
        super("거리는 음수가 될 수 없습니다 (입력값: " + value + ")");
    }

}
