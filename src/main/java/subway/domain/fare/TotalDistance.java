package subway.domain.fare;

import java.util.Objects;

public class TotalDistance {
    private final int distance;

    public TotalDistance(final int distance) {
        this.distance = distance;
    }

    public TotalDistance add(final TotalDistance distance) {
        return new TotalDistance(this.distance + distance.distance());
    }

    public TotalDistance subtract(final TotalDistance distance) {
        return new TotalDistance(this.distance - distance.distance);
    }

    public TotalDistance divideAndCeil(final TotalDistance distance) {
        final double dividedDistance = (double) this.distance / distance.distance;
        return new TotalDistance((int) Math.ceil(dividedDistance));
    }

    public boolean lessThan(final TotalDistance distance) {
        return this.distance < distance.distance;
    }

    public boolean lessAndEqualsThan(final TotalDistance distance) {
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
        final TotalDistance that = (TotalDistance) o;
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
