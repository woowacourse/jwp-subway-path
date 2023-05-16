package subway.domain.fare;

public class DistanceFarePolicy implements FarePolicy {

    private static final int BASIC_DISTANCE = 10;
    private static final int BASIC_FARE = 1250;
    private static final int PER_KILOMETER_UNIT = 5;
    private static final int MONEY_UNIT = 100;

    @Override
    public int calculateOverFare(final double distance){
        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARE;
        }
        return BASIC_FARE + (int) ((Math.ceil((distance - BASIC_DISTANCE) / PER_KILOMETER_UNIT)) * MONEY_UNIT);
    }
}
