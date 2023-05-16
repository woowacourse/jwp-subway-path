package subway.domain;

import java.util.Objects;

public class Distance {
    private final int value;

    public Distance(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < 1) {
            throw new IllegalArgumentException("거리는 1 이상만 가능합니다.");
        }
    }

    public Distance sub(Distance distance) {
        return new Distance(value - distance.value);
    }

    public Distance sum(Distance distance) {
        return new Distance(value + distance.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
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
