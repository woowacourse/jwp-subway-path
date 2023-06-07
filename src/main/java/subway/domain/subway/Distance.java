package subway.domain.subway;

import java.util.Objects;

public class Distance {

    private int distance;

    public Distance(final int distance) {
        this.distance = distance;
    }

    public Distance plusDistance(final int distance) {
        return new Distance(this.distance += distance);
    }

    public Distance minusDistance(final int distance) {
        return new Distance(this.distance -= distance);
    }

    public int getDistance() {
        return distance;
    }

    public boolean isShorterSame(final int distance) {
        return this.distance < distance;
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
