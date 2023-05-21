package subway.domain.fare;

public class FareCalculator {

    private final FarePolicy farePolicy;

    public FareCalculator(FarePolicy farePolicy) {
        this.farePolicy = farePolicy;
    }

    public int calculate(int distance) {
        return farePolicy.calculateFare(distance);
    }
}
