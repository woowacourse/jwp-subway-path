package subway.application.core.domain;

import subway.application.core.exception.DistanceNotPositiveException;

import java.util.Objects;

public class Distance {

    private final int distance;

    public Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(int distance) {
        if (distance <= 0) {
            throw new DistanceNotPositiveException();
        }
    }

    public Distance add(Distance targetDistance) {
        return new Distance(distance + targetDistance.distance);
    }

    public Distance sub(Distance targetDistance) {
        return new Distance(distance - targetDistance.distance);
    }

    public boolean isBiggerThan(Distance target) {
        return distance > target.distance;
    }

    public int value() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance other = (Distance) o;
        return distance == other.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
