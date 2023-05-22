package subway.domain;

import subway.exceptions.IllegalDistanceException;

import java.util.Objects;

public class Distance {
    private static final int MIN_VALUE = 1;

    private final int value;

    public Distance(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < MIN_VALUE) {
            throw new IllegalDistanceException("거리는 1 이상만 가능합니다.");
        }
    }

    public Distance sub(final Distance distance) {
        if (value - distance.value < MIN_VALUE) {
            throw new IllegalDistanceException("거리 계산 결과는 음수 또는 0이 될 수 없습니다");
        }
        return new Distance(value - distance.value);
    }

    public Distance sum(final Distance distance) {
        return new Distance(value + distance.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public int getValue() {
        return value;
    }
}
