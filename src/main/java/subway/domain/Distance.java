package subway.domain;

import java.util.Objects;

public class Distance {

    private static final Distance ZERO = new Distance(0);
    
    private final int distance;

    private Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    public static Distance from(double distance) {
        return new Distance((int) distance);
    }

    private static void validateDistance(int distance) {
        if (distance < 0) {
            throw new IllegalStateException("거리는 음수 값이 나올 수 없습니다.");
        }
    }

    public static Distance zero() {
        return ZERO;
    }

    public boolean isShorterThanAndEqual(Distance distance) {
        return this.distance <= distance.getDistance();
    }

    public boolean isShorterThan(Distance distance) {
        return this.distance < distance.getDistance();
    }

    public Distance plus(Distance distance) {
        return new Distance(this.distance + distance.getDistance());
    }

    public Distance minus(Distance distance) {
        return new Distance(this.distance - distance.getDistance());
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

    @Override
    public String toString() {
        return "Distance{" +
                "distance=" + distance +
                '}';
    }

    public int getDistance() {
        return distance;
    }
}
