package subway.domain;

import java.util.Objects;

public class Distance {

    private final int value;

    private Distance() {
        this.value = 0;
    }

    public Distance(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("거리는 0 이하일 수 없습니다.");
        }
    }

    public Distance minus(final Distance other) {
        return new Distance(this.value - other.value);
    }

    public Distance sum(final Distance other) {
        return new Distance(this.value + other.value);
    }

    public boolean equalToSumOf(final Distance upDistance, final Distance downDistance) {
        return this.value == upDistance.value + downDistance.value;
    }

    public boolean isSameOrOverThan(final Distance other) {
        return this.value >= other.value;
    }

    public int getValue() {
        return value;
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

    @Override
    public String toString() {
        return "Distance{" +
                "value=" + value +
                '}';
    }
}
