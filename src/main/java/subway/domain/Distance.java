package subway.domain;

public class Distance {

    private static final int MIN_VALUE = 1;
    private final int value;

    public Distance(final int value) {
        validateDistance(value);
        this.value = value;
    }

    private void validateDistance(final int value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("거리는 1이상의 정수여야합니다.");
        }
    }

    public int getValue() {
        return value;
    }

    public Distance minusValue(final Distance distance) {
        return new Distance(this.value - distance.value);
    }

    public Distance plusValue(final Distance distance) {
        return new Distance(this.value + distance.value);
    }
}
