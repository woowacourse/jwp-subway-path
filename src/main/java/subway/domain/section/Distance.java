package subway.domain.section;

import subway.exception.IllegalDistanceException;

import java.util.Objects;

public class Distance {

    public static final int MIN_DISTANCE = 0;
    public static final int MAX_DISTANCE = 100;
    private final int distance;

    public Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(int distance) {
        if (distance < MIN_DISTANCE || distance > MAX_DISTANCE) {
            throw new IllegalDistanceException();
        }
    }

    public Distance subtract(Distance targetDistance) {
        return new Distance(this.distance - targetDistance.distance);
    }

    public Distance add(Distance targetDistance) {
        return new Distance(this.distance + targetDistance.distance);
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
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

    @Override
    public String toString() {
        return "Distance{" +
                "distance=" + distance +
                '}';
    }
}
