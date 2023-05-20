package subway.domain;

public final class FareCalculator {

    private static final int BASIC_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int FIRST_BOUND = 50;
    private static final int FIRST_BOUND_EXCEED_UNIT = 8;
    private static final int SECOND_BOUND = 10;
    private static final int SECOND_BOUND_EXCEED_UNIT = 5;

    private FareCalculator() {

    }

    public static int calculate(final Line line, int distance, final int age) {
        if (distance == 0) {
            return 0;
        }
        final int fare = calculateFare(line, distance);
        return PassengerAge.get(age).getDiscountedPrice(fare);
    }

    private static int calculateFare(final Line line, int distance) {
        int totalFare = BASIC_FARE;

        if (distance > FIRST_BOUND) {
            totalFare += getOveredFare(distance - FIRST_BOUND, FIRST_BOUND_EXCEED_UNIT);
            distance = FIRST_BOUND;
        }

        if (distance > SECOND_BOUND) {
            totalFare += getOveredFare(distance - SECOND_BOUND, SECOND_BOUND_EXCEED_UNIT);
        }

        return totalFare + line.getAdditionalFare();
    }

    private static int getOveredFare(final int distance, final int bound) {
        int exceedCount = distance / bound;
        if (distance % bound != 0) {
            exceedCount++;
        }
        return exceedCount * ADDITIONAL_FARE;
    }
}


