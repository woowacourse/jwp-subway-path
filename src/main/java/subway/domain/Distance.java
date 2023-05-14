package subway.domain;

import java.util.Objects;

public class Distance {

    private final int value;

    public Distance(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("길이는 1이상이어야 합니다.");
        }
    }

    public Distance minus(final Distance otherDistance) {
        return new Distance(this.value - otherDistance.value);
    }

    public boolean isSameOrOver(final Distance other) {
        return this.value >= other.value;
    }

    public boolean isOver(final Distance other) {
        return this.value > other.value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Distance 거리10 = (Distance) o;
        return value == 거리10.value;
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
