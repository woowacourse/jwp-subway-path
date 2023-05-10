package subway.domain.vo;

public class Distance {

    private static final int MIN_VALUE = 0;

    private final int value;

    public Distance(int value) {
        validatePositive(value);
        this.value = value;
    }

    private void validatePositive(int value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("거리는 음수가 될 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
