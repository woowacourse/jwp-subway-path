package subway.domain;

import java.util.Objects;
import subway.exception.distance.InvalidDistanceLengthException;

public class Distance {
    private static final int MIN_DISTANCE = 1;

    private final int distance;

    public Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(int distance) {
        if (distance < MIN_DISTANCE) {
            throw new InvalidDistanceLengthException("역간 최소 거리는 1km 입니다.");
        }
    }

    public int getDistance() {
        return distance;
    }

    public boolean isBiggerThanOtherDistance(Distance otherDistance) {
        return this.distance > otherDistance.distance;
    }

    public Distance subtract(Distance otherDistance) {
        return new Distance(this.distance - otherDistance.distance);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }

}
