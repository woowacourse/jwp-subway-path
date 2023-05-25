package subway.domain;

import subway.exception.InvalidDistanceException;

import java.util.Objects;

public final class Distance {

    private static final int BOUNDARY_POINT = 0;
    private static final Distance ZERO_DISTANCE = new Distance(0);

    private final int length;

    public Distance(final int length) {
        this.length = length;
    }

    public static Distance getInitialDistance() {
        return ZERO_DISTANCE;
    }

    public Distance subtract(final Distance distance) {
        final int subtractLength = length - distance.length;
        validPositive(subtractLength);
        return new Distance(subtractLength);
    }

    public Distance add(final Distance distance) {
        return new Distance(length + distance.length);
    }

    private void validPositive(final int length) {
        if (length <= BOUNDARY_POINT) {
            throw new InvalidDistanceException();
        }
    }

    public boolean isGreaterThan(final Distance distance) {
        return length > distance.length;
    }

    public boolean isLessThanOrEqualTo(final Distance distance) {
        return length <= distance.length;
    }

    public int getLength() {
        return length;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Distance)) return false;
        Distance distance = (Distance) o;
        return length == distance.length;
    }

    @Override
    public int hashCode() {
        return Objects.hash(length);
    }
}
