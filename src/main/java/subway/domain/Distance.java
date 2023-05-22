package subway.domain;

import java.util.Objects;
import subway.exception.DistanceValueValidateException;

public class Distance {

    private static final int MIN_VALUE = 1;

    private final int value;

    public Distance(final int value) {
        validateDistance(value);
        this.value = value;
    }

    private void validateDistance(final int value) {
        if (value < MIN_VALUE) {
            throw new DistanceValueValidateException();
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

    public boolean isBigger(final Distance distance) {
        return this.value > distance.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
