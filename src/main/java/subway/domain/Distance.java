package subway.domain;

public class Distance {

    private final int value;

    public Distance(int value) {
        validateMinValue(value);
        this.value = value;
    }

    private void validateMinValue(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("거리는 음수가 될 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
