package subway.domain;

import subway.application.exception.SubwayServiceException;

import java.util.Objects;

public class Distance {

    private static final String INVALID_DISTANCE_MESSAGE = "거리는 양의 정수여야 합니다.";
    private static final int MIN_DISTANCE = 0;

    private final int value;

    public Distance(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value <= MIN_DISTANCE) {
            throw new SubwayServiceException(INVALID_DISTANCE_MESSAGE);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
