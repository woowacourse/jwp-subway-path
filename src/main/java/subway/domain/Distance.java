package subway.domain;

import java.util.Objects;

public class Distance {

    private static final Integer MIN_DISTANCE = 1;
    private final Integer value;

    public Distance(Integer value) {
        this.value = value;
        validate(this.value);
    }

    private void validate(Integer value) {
        if(value <= MIN_DISTANCE) {
            throw new IllegalArgumentException("역 사이의 거리는 양수여야 합니다");
        }
    }

    public boolean isLongerThan(Distance other) {
        return this.value.compareTo(other.getValue()) > 0;
    }

    public Distance subtract(Distance other) {
        return new Distance(this.value - other.value);
    }

    public Distance plus(Distance other) {
        return new Distance(this.value + other.value);

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
        return Objects.equals(value, distance.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public Integer getValue() {
        return value;
    }
}
