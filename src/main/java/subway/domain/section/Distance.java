package subway.domain.section;

import java.util.Objects;

public class Distance {

    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 100;
    private final int value;

    public Distance(final int value) {
        validateDistance(value);
        this.value = value;
    }

    private void validateDistance(final int value) {
        if (MAX_RANGE < value || value < MIN_RANGE) {
            throw new IllegalArgumentException("거리는 " + MIN_RANGE + "이상 " + MAX_RANGE + "이하여야 합니다.");
        }
    }

    public int getValue() {
        return value;
    }

    public Distance minusValue(final Distance distance) {
        return new Distance(this.value - distance.value);
    }

    public Distance plusValue(final Distance distance) {
        return new Distance(this.value + distance.value);
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
