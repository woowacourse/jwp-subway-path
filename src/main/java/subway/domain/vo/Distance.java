package subway.domain.vo;

import java.util.Objects;
import subway.domain.exception.IllegalDistanceArgumentException;

public class Distance {

    private final int value;

    public Distance(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value <= 0) {
            throw new IllegalDistanceArgumentException("거리는 양의 정수여야 합니다.");
        }
    }

    public Distance minus(Distance distance) {
        return new Distance(value - distance.value);
    }

    public Distance plus(Distance distance) {
        return new Distance(value + distance.value);
    }

    public boolean isWithIn(Distance distance) {
        return value <= distance.value;
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

    @Override
    public String toString() {
        return "Distance{" +
                "value=" + value +
                '}';
    }
}
