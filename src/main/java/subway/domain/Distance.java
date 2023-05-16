package subway.domain;

import java.util.Objects;

public class Distance {
    private final Integer value;

    public Distance(final Integer distance) {
        validatePositive(distance);
        this.value = distance;
    }

    private void validatePositive(final Integer distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("등록하려는 역의 거리 정보가 잘못되어 등록에 실패했습니다.");
        }
    }

    public Distance plus(final Distance distance) {
        return new Distance(value + distance.getValue());
    }

    public Distance minus(final Distance distance) {
        return new Distance(value - distance.getValue());
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return Objects.equals(value, distance1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
