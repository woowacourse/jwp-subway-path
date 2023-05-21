package subway.domain;

import java.util.Objects;

public class Distance {
    private final int value;

    public Distance(final int value) {
        validatePositive(value);
        this.value = value;
    }

    private void validatePositive(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("구간(역-역)의 거리는 1 이상이어야 합니다.");
        }
    }

    public Distance add(Distance other) {
        return new Distance(this.value + other.value);
    }

    public Distance minus(Distance other) {
        return new Distance(Math.abs(this.value - other.value));
    }

    public boolean greaterThan(Distance other) {
        return value > other.value;
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
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
