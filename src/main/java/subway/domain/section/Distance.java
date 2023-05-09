package subway.domain.section;

import subway.exception.InvalidDistanceException;

public class Distance {

    private static final int MINIMUM_VALUE = 0;
    private static final int MAXIMUM_VALUE = 15;

    private final int value;

    public Distance(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < MINIMUM_VALUE || value > MAXIMUM_VALUE) {
            throw new InvalidDistanceException("역 사이의 거리는 0이상, 15이하여야합니다.");
        }
    }
}
