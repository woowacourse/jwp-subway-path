package subway.domain;

import java.util.Objects;

public class Distance {

    public static final Distance emptyDistance = new Distance();
    private static final int MIN = 1;
    private final Integer value;

    public Distance(Integer value) {
        validatePositive(value);
        this.value = value;
    }

    private Distance() {
        this.value = 0;
    }

    private void validatePositive(Integer value) {
        if (value < MIN) {
            throw new IllegalArgumentException("거리는 양의 정수여야 합니다");
        }
    }

    public Distance minus(Distance distance) {
        return new Distance(value - distance.value);
    }

    public Distance plus(Distance distance) {
        return new Distance(value + distance.value);
    }

    public int getValue() {
        return value;
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
}
