package subway.domain;

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

    public int getValue() {
        return value;
    }

}
