package subway.domain.core;

import java.util.Objects;
import subway.exception.DistanceNotValidException;

public class Distance {

    private static final int MINIMUM_DISTANCE_VALUE = 1;

    private final int value;
    
    public Distance(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < MINIMUM_DISTANCE_VALUE) {
            throw new DistanceNotValidException();
        }
    }

    public boolean moreThanOrEqual(final Distance distance) {
        return value <= distance.value;
    }

    public Distance subtract(final Distance distance) {
        return new Distance(value - distance.value);
    }

    public Distance add(final Distance distance) {
        return new Distance(value + distance.value);
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

    @Override
    public String toString() {
        return "Distance{" +
                "value=" + value +
                '}';
    }

    public int getValue() {
        return value;
    }
}
