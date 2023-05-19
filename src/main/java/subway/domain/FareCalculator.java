package subway.domain;

public class FareCalculator {

    private static final int BASIC_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int FIRST_BOUND = 10;
    private static final int FIRST_BOUND_UNIT = 5;
    private static final int SECOND_BOUND = 50;
    private static final int SECOND_BOUND_UNIT = 8;

    private FareCalculator() {

    }

    public static int calculate(final Line line, double distance) {
        if (distance == 0) {
            return 0;
        }
        int currentFare = BASIC_FARE;

        if (distance > SECOND_BOUND) {
            currentFare += (((int) distance - SECOND_BOUND) / SECOND_BOUND_UNIT) * ADDITIONAL_FARE;
            if ((int) distance % SECOND_BOUND != 0) {
                currentFare += ADDITIONAL_FARE;
            }
            distance = SECOND_BOUND;
        }

        if (distance > FIRST_BOUND) {
            currentFare += (((int) distance - FIRST_BOUND) / FIRST_BOUND_UNIT) * ADDITIONAL_FARE;
            if ((int) distance % FIRST_BOUND != 0) {
                currentFare += ADDITIONAL_FARE;
            }
        }

        return currentFare + line.getAdditionalFee();
    }
}


