package subway.domain;

public class Fare {

    private static final int DEFAULT_FARE_DISTANCE = 10;
    private static final int EXTRA_FARE_DISTANCE = 50;
    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_FARE_OVER_FIFTY = 2050;
    private static final int EXTRA_FARE_UNIT = 5;
    private static final int EXTRA_FARE_UNIT_OVER_FIFTY = 8;

    private Fare() {
    }

    public static int calculate(final int distance) {
        if (distance <= DEFAULT_FARE_DISTANCE) {
            return DEFAULT_FARE;
        }
        return addExtraFare(distance);
    }

    private static int addExtraFare(final int distance) {
        if (distance <= EXTRA_FARE_DISTANCE) {
            return DEFAULT_FARE + calculateExtraFare(distance - DEFAULT_FARE_DISTANCE, EXTRA_FARE_UNIT);
        }
        return DEFAULT_FARE_OVER_FIFTY +
                calculateExtraFare(distance - EXTRA_FARE_DISTANCE, EXTRA_FARE_UNIT_OVER_FIFTY);
    }

    private static int calculateExtraFare(final int distance, final int unit) {
        return (((distance - 1) / unit) + 1) * 100;
    }
}
