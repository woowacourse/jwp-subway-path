package subway.domain;

public class SubwayFareCalculator implements FareCalculator {

    private static final Fare DEFAULT_FARE = new Fare(1250);
    private static final Fare ADDITIONAL_FARE = new Fare(100);
    private static final Distance DEFAULT_DISTANCE = new Distance(11);
    private static final Distance DISTANCE_THRESHOLD = new Distance(51);
    private static final int INITIAL_INCREASE_DISTANCE = 5;
    private static final int SECONDARY_INCREASE_DISTANCE = 8;
    private static final int COUNT_TO_INITIAL_INCREASE = 8;

    @Override
    public Fare calculate(final Distance distance) {
        if (distance.isLessThan(DEFAULT_DISTANCE)) {
            return DEFAULT_FARE;
        }
        if (distance.isLessThan(DISTANCE_THRESHOLD)) {
            return DEFAULT_FARE.add(calculateInitialAdditionalFare(distance));
        }
        return DEFAULT_FARE.add(calculateSecondaryAdditionalFare(distance));
    }

    private Fare calculateInitialAdditionalFare(final Distance distance) {
        final int count = distance.subtract(DEFAULT_DISTANCE) / INITIAL_INCREASE_DISTANCE + 1;
        return ADDITIONAL_FARE.multiply(count);
    }

    private Fare calculateSecondaryAdditionalFare(final Distance distance) {
        final int count = distance.subtract(DISTANCE_THRESHOLD) / SECONDARY_INCREASE_DISTANCE + 1;
        return ADDITIONAL_FARE.multiply(COUNT_TO_INITIAL_INCREASE + count);
    }
}
