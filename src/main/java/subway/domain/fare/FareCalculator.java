package subway.domain.fare;

public class FareCalculator {
    private final DistancePolicy distancePolicy;

    public FareCalculator(final DistancePolicy distanceChain) {
        this.distancePolicy = distanceChain;
    }

    public int calculate(final int distance) {
        return distancePolicy.calculate(distance);
    }
}
