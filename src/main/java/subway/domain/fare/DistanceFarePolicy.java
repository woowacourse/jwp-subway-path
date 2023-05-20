package subway.domain.fare;

public class DistanceFarePolicy implements FarePolicy {

    private static final int DEFAULT_DISTANCE = 10;
    private static final long DEFAULT_FARE = 1_250L;

    private static final int ADDITIONAL_DISTANCE_PER_TEN = 10;
    private static final long ADDITIONAL_FARE_PER_TEN = 100L;
    private static final int DISTANCE_UNIT_PER_TEN = 5;

    private static final int ADDITIONAL_DISTANCE_PER_FIFTY = 50;
    private static final long ADDITIONAL_FARE_PER_FIFTY = 100L;
    private static final int DISTANCE_UNIT_PER_FIFTY = 8;

    @Override
    public FareAmount calculate(final int totalDistance) {
        if (isDefaultFareDistance(totalDistance)) {
            return FareAmount.from(DEFAULT_FARE);
        }

        return calculateTotalFare(totalDistance);
    }

    private boolean isDefaultFareDistance(final int distance) {
        return DEFAULT_DISTANCE >= distance;
    }

    private FareAmount calculateTotalFare(final int distance) {
        if (ADDITIONAL_DISTANCE_PER_FIFTY > distance) {
            return calculateTotalFareByTen(distance);
        }

        return calculateTotalFareByFifty(distance);
    }

    private FareAmount calculateTotalFareByTen(final int distance) {
        final long additionalFare = calculateAdditionalFare(
                        distance - ADDITIONAL_DISTANCE_PER_TEN,
                        ADDITIONAL_FARE_PER_TEN, DISTANCE_UNIT_PER_TEN
        );

        final long totalFare = DEFAULT_FARE + additionalFare;

        return FareAmount.from(totalFare);
    }

    private FareAmount calculateTotalFareByFifty(final int distance) {
        final long additionalFareByTen = calculateAdditionalFare(
                ADDITIONAL_DISTANCE_PER_FIFTY - DEFAULT_DISTANCE,
                ADDITIONAL_FARE_PER_TEN,
                DISTANCE_UNIT_PER_TEN
        );
        final long additionalFareByFifty = calculateAdditionalFare(
                distance - ADDITIONAL_DISTANCE_PER_FIFTY,
                ADDITIONAL_FARE_PER_FIFTY,
                DISTANCE_UNIT_PER_FIFTY
        );
        final long totalFare = DEFAULT_FARE + additionalFareByTen + additionalFareByFifty;

        return FareAmount.from(totalFare);
    }

    private long calculateAdditionalFare(final int distance, final long farePerUnit, final int distanceUnit) {
        return (((distance - 1) / distanceUnit) + 1) * farePerUnit;
    }
}
