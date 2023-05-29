package subway.domain;

import java.util.Objects;

public class Distance {

    private static final int MIN_DISTANCE = 1;
    private final int value;

    public Distance(int value) {
        this.value = value;
        validate(this.value);
    }

    private void validate(int value) {
        if(value <= MIN_DISTANCE) {
            throw new IllegalArgumentException("역 사이의 거리는 양수여야 합니다");
        }
    }

    public boolean isLongerThan(Distance other) {
        return this.value > other.getValue();
    }

    public Distance subtract(Distance other) {
        if(other.isLongerThan(this)) {
            throw new IllegalArgumentException("더 큰 거리를 뺄 수 없습니다.");
        }
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

    public int getValue() {
        return value;
    }
}
