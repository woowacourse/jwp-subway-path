package subway.domain.section;

import subway.exception.InvalidDistanceException;

final class Distance {

    private static final int MINIMUM_VALUE = 0;

    private final int value;

    public Distance(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < MINIMUM_VALUE) {
            throw new InvalidDistanceException("역 사이의 거리는 0이상이어야합니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
