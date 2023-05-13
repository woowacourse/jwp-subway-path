package subway.domain.common;

import subway.exception.DistanceForkedException;
import subway.exception.DistanceValueInvalidException;

import java.util.Objects;

public class Distance {

    private static final int MINIMUM_DISTANCE = 1;

    private final long distance;

    public Distance(final long distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(final long distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new DistanceValueInvalidException();
        }
    }

    public void validateSectionDistance(final long requestDistance) {
        if (distance <= requestDistance) {
            throw new DistanceForkedException();
        }
    }

    public long getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Distance)) return false;
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
