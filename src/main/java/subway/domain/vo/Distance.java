package subway.domain.vo;

import java.util.Objects;

public class Distance {

    public static final Distance ZERO = new Distance(0);

    private final int value;

    private Distance(final int value) {
        validate(value);
        this.value = value;
    }

    public static Distance from(final int value) {
        return new Distance(value);
    }

    private void validate(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("길이는 0이상이어야 합니다.");
        }
    }

    public Distance minus(final Distance otherDistance) {
        return new Distance(this.value - otherDistance.value);
    }

    public Distance plus(final Distance otherDistance) {
        return new Distance(this.value + otherDistance.value);
    }

    public Distance quotient(final Distance otherDistance) {
        return new Distance(value / otherDistance.value);
    }

    public boolean isEqualsOrGreaterThan(final Distance other) {
        return value >= other.value;
    }

    public boolean isZero() {
        return value == 0;
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
