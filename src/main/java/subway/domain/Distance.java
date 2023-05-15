package subway.domain;

import java.util.Objects;

public class Distance {

    private final int value;

    public Distance(final int value) {
        this.value = value;
    }

    public boolean isShorterThan(final Distance other) {
        return value <= other.value;
    }

    public Distance minus(final Distance other) {
        return new Distance(value - other.value);
    }

    public Distance plus(final Distance other) {
        return new Distance(value + other.value);
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
}
