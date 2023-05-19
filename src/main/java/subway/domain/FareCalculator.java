package subway.domain;

public class FareCalculator {

    private static final int BASIC_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int SECOND_BOUND = 10;
    private static final int SECOND_BOUND_UNIT = 5;
    private static final int FIRST_BOUND = 50;
    private static final int FIRST_BOUND_UNIT = 8;
    private static final int DEDUCTIBLE_AMOUNT = 350;
    private static final int KID_LOWER_BOUND = 6;
    private static final int KID_UPPER_BOUND = 12;
    private static final int TEENAGER_LOWER_BOUND = 13;
    private static final int TEENAGER_UPPER_BOUND = 18;

    private FareCalculator() {

    }

    public static int calculate(final Line line, int distance, final int age) {
        final int fare = calculateFare(line, distance);
        return fare - discount(fare, age);
    }

    private static int calculateFare(final Line line, int distance) {
        if (distance == 0) {
            return 0;
        }
        int currentFare = BASIC_FARE;

        if (distance > FIRST_BOUND) {
            currentFare += ((distance - FIRST_BOUND) / FIRST_BOUND_UNIT) * ADDITIONAL_FARE;
            if (distance % FIRST_BOUND != 0) {
                currentFare += ADDITIONAL_FARE;
            }
            distance = FIRST_BOUND;
        }

        if (distance > SECOND_BOUND) {
            currentFare += ((distance - SECOND_BOUND) / SECOND_BOUND_UNIT) * ADDITIONAL_FARE;
            if (distance % SECOND_BOUND != 0) {
                currentFare += ADDITIONAL_FARE;
            }
        }

        return currentFare + line.getAdditionalFee();
    }

    private static int discount(final int fare, final int age) {
        if (fare <= DEDUCTIBLE_AMOUNT) {
            return 0;
        }
        if (KID_LOWER_BOUND <= age && age <= KID_UPPER_BOUND) {
            return (fare - DEDUCTIBLE_AMOUNT) / 2;
        }
        if (TEENAGER_LOWER_BOUND <= age && age <= TEENAGER_UPPER_BOUND) {
            return (fare - DEDUCTIBLE_AMOUNT) / 5;
        }
        return 0;
    }
}


