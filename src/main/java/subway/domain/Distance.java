package subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Distance {
    private final int value;

    public Distance(final int value) {
        validatePositive(value);
        this.value = value;
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

    private void validatePositive(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("구간(역-역)의 거리는 1 이상이어야 합니다.");
        }
    }
}
