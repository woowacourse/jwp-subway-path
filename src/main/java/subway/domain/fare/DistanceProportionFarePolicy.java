package subway.domain.fare;

public class DistanceProportionFarePolicy {

    private static final int BASE_DISTANCE_MAX = 10;
    private static final int EXTRA_DISTANCE_MAX = 50;
    private static final int BASE_DISTANCE_UNIT = 5;
    private static final int OVER_EXTRA_DISTANCE_UNIT = 8;
    private static final Fare BASE_FARE = new Fare(1_250);
    private static final Fare OVER_EXTRA_FARE = new Fare(2_050);
    private static final int SURCHARGE_FARE = 100;

    public Fare calculate(final int distance) {
        if (distance <= BASE_DISTANCE_MAX) {
            return BASE_FARE;
        }

        if (distance <= EXTRA_DISTANCE_MAX) {
            return calculateExtraFare(distance);
        }

        return calculateOverExtraFare(distance);
    }

    private Fare calculateExtraFare(final int distance) {
        final int extraDistance = distance - BASE_DISTANCE_MAX;
        final int extraFare = (int) (Math.ceil((extraDistance - 1) / BASE_DISTANCE_UNIT) + 1) * SURCHARGE_FARE;

        return BASE_FARE.add(new Fare(extraFare));
    }

    private Fare calculateOverExtraFare(final int distance) {
        final int extraDistance = distance - EXTRA_DISTANCE_MAX;
        final int extraFare = (int) (Math.ceil((extraDistance - 1) / OVER_EXTRA_DISTANCE_UNIT) + 1) * SURCHARGE_FARE;

        return OVER_EXTRA_FARE.add(new Fare(extraFare));
    }
}
