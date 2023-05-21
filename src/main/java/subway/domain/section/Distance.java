package subway.domain.section;

import java.util.Objects;

public class Distance {

    private static final int MAX_DISTANCE = 100;

    private final int distance;

    public Distance(int distance) {
        validateLength(distance);
        this.distance = distance;
    }

    private void validateLength(int distance) {
        if (distance > MAX_DISTANCE) {
            throw new IllegalArgumentException("역 사이 거리는 100km 이하여야 합니다.");
        }
    }

    public Distance subtract(Distance targetDistance) {
        return new Distance(this.distance - targetDistance.getDistance());
    }

    public boolean isGreaterThanOrEqual(Distance targetDistance) {
        return distance >= targetDistance.getDistance();
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
