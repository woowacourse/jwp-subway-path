package subway.domain.fare;

import java.util.Objects;

public class SumDistance {
    private final int distance;

    public SumDistance(final int distance) {
        this.distance = distance;
    }

    public SumDistance add(final SumDistance distance) {
        return new SumDistance(this.distance + distance.distance());
    }

    public SumDistance subtract(final SumDistance distance) {
        return new SumDistance(this.distance - distance.distance);
    }

    public SumDistance divideAndCeil(final SumDistance distance) {
        final double dividedDistance = (double) this.distance / distance.distance;
        return new SumDistance((int) Math.ceil(dividedDistance));
    }

    public boolean lessThan(final SumDistance distance) {
        return this.distance < distance.distance;
    }

    public boolean lessAndEqualsThan(final SumDistance distance) {
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
        final SumDistance that = (SumDistance) o;
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
