package subway.domain;

import java.util.Objects;
import subway.exception.DistanceNotValidException;

public class Distance implements Comparable<Distance> {

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

    public Distance subtract(final Distance distance) {
        return new Distance(value - distance.value);
    }

    public Distance add(final Distance distance) {
        return new Distance(value + distance.value);
    }

    @Override
    public int compareTo(final Distance comparedDistance) {
        if (this.value > comparedDistance.value) {
            return 1;
        }
        else if (this.value == comparedDistance.value) {
            return 0;
        }
        return -1;
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
