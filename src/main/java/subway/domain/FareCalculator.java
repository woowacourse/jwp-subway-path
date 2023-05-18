package subway.domain;

public class FareCalculator {

    private static final int BASIC_DISTANCE = 10;
    private static final int ADDITIONAL_DISTANCE = 50;
    private static final int BASIC_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;

    private FareCalculator(){

    }

    public static int calculate(final int distance) {
        if (distance <= 0) {
            throw new IllegalStateException("잘못된 경로");
        }

        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARE;
        }

        if (distance <= ADDITIONAL_DISTANCE) {
            return BASIC_FARE + ADDITIONAL_FARE * (int) Math.ceil((distance - BASIC_DISTANCE) / 5.0);
        }

        return BASIC_FARE + ADDITIONAL_FARE * (ADDITIONAL_DISTANCE - BASIC_DISTANCE) / 5
                + ADDITIONAL_FARE * (int) Math.ceil((distance - ADDITIONAL_DISTANCE) / 8.0);
    }
}
