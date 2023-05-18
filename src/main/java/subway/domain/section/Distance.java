package subway.domain.section;

import subway.exception.distance.InvalidDistanceException;

import java.util.Objects;

public class Distance {

    private final int distance;

    public Distance(final int distance) {
        validateDistanceIsPositive(distance);
        this.distance = distance;
    }

    private void validateDistanceIsPositive(final int distance) {
        if (distance <= 0) {
            throw new InvalidDistanceException();
        }
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
