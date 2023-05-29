package subway.domain.fare;

public abstract class DistanceFarePolicy implements FarePolicy {

    private static final int FARE = 100;

    public int calculateAdditionalFare(final int distance, final double increment) {
        return (int) Math.ceil(distance / increment) * FARE;
    }
}
