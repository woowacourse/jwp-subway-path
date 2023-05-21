package subway.domain;

import java.util.Objects;
import subway.exception.section.DistanceNotPositiveException;

public class Fare {

    private final int value;

    public Fare(final FarePolicy farePolicy, final int distance) {
        validateDistance(distance);
        this.value = farePolicy.calculateFare(distance);
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new DistanceNotPositiveException("거리는 0 이하일 수 없습니다.");
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
        final Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
