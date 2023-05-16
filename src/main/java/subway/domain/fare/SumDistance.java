package subway.domain.fare;

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

    public int distance() {
        return distance;
    }
}
