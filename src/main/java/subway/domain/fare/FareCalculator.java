package subway.domain.fare;

public class FareCalculator {
    private final DistancePolicy distancePolicy;

    public FareCalculator(DistancePolicy distanceChain) {
        this.distancePolicy = distanceChain;
    }

    public int calculate(int distance) {
        return distancePolicy.calculate(distance);
    }
}
