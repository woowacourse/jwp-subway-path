package subway.domain;

import java.util.Objects;

public class Distance {
    private final int value;

    public Distance(int value) {
        validate(value);
        this.value = value;
    }

    public static Distance from(int value) {
        return new Distance(value);
    }

    private void validate(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("거리는 양수여야 합니다.");
        }
    }

    public Distance plus(Distance distance) {
        return new Distance(value + distance.value);
    }

    public Distance minus(Distance distance) {
        return new Distance(value - distance.value);
    }


    @Override
    public boolean equals(Object o) {
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

    public int getValue() {
        return value;
    }

    public boolean isLongerThan(Distance otherDistance) {
        return value > otherDistance.value;
    }

    public boolean isShorterThan(Distance otherDistance) {
        return value < otherDistance.value;
    }
}
