package subway.domain;

import java.util.Objects;

public class Distance {

    private int value;

    private Distance() {
    }

    public Distance(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value > 0) {
            return;
        }
        throw new IllegalArgumentException("거리는 양의 정수만 가능합니다.");
    }

    public boolean isNotGreaterThan(final Distance other) {
        return value <= other.value;
    }

    public Distance subtract(final Distance other) {
        return new Distance(value - other.value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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
