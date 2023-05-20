package subway.domain.route;

import java.util.Objects;

public class Distance {

    private final int distance;

    public Distance(final int distance) {
        this.distance = distance;
    }

    public Distance add(final Distance distance) {
        return new Distance(this.distance + distance.distance());
    }

    public Distance subtract(final Distance distance) {
        return new Distance(this.distance - distance.distance);
    }

    public Distance divideAndCeil(final Distance distance) {
        final double dividedDistance = (double) this.distance / distance.distance;
        return new Distance((int) Math.ceil(dividedDistance));
    }

    public boolean moreThan(final Distance distance) {
        return this.distance > distance.distance;
    }

    public boolean lessAndEqualsThan(final Distance distance) {
        return this.distance <= distance.distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Distance that = (Distance) o;
        return distance == that.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }

    public int distance() {
        return distance;
    }
}
