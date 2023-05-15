package subway.domain.fare;

public class DistanceFarePolicy implements FarePolicy {

    private static final int PER_KILOMETER_UNIT = 5;
    private static final int MONEY_UNIT = 100;

    @Override
    public int calculateOverFare(final double distance) {
        return (int) ((Math.ceil((distance - 1) / PER_KILOMETER_UNIT) + 1) * MONEY_UNIT);
    }
}
