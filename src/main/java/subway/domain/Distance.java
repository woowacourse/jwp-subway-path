package subway.domain;

import java.util.Objects;

public class Distance {

    private static final Distance ZERO = new Distance(0);
    
    private final int distance;

    public Distance(int distance) {
        this.distance = distance;
    }

    public static Distance zero() {
        return ZERO;
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

    public int getDistance() {
        return distance;
    }
}
