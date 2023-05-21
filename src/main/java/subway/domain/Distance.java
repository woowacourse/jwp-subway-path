package subway.domain;

import subway.exception.InvalidDistanceException;

public final class Distance {

    private static final int BOUNDARY_POINT = 0;

    private final int length;

    public Distance(final int length) {
        this.length = length;
    }

    public Distance subtract(final Distance distance) {
        final int subtractLength = length - distance.length;
        validPositive(subtractLength);
        return new Distance(subtractLength);
    }

    private void validPositive(final int length) {
        if (length <= BOUNDARY_POINT) {
            throw new InvalidDistanceException();
        }
    }

    public boolean biggerThan(final Distance distance) {
        return length > distance.length;
    }

    public boolean lessThanOrEqualTo(final Distance distance) {
        return length <= distance.length;
    }

    public int getLength() {
        return length;
    }
}
