package subway.domain;

public class FareCalculator {

    private static final int STANDARD_FARE = 1_250;
    private static final Long STANDARD_LOWER_DISTANCE = 10L;
    private static final Long STANDARD_UPPER_DISTANCE = 50L;


    public static int calculate(Long distance) {
        if (distance > STANDARD_LOWER_DISTANCE && distance <= STANDARD_UPPER_DISTANCE) {
            return STANDARD_FARE + calculateOverFareWhenNotExceedUpperDistance(distance - STANDARD_LOWER_DISTANCE);
        }
        if (distance > STANDARD_UPPER_DISTANCE) {
            return STANDARD_FARE
                    + calculateOverFareWhenNotExceedUpperDistance(STANDARD_UPPER_DISTANCE - STANDARD_LOWER_DISTANCE)
                    + calculateOverFareWhenExceedUpperDistance(distance - STANDARD_UPPER_DISTANCE);
        }
        return STANDARD_FARE;
    }

    private static int calculateOverFareWhenNotExceedUpperDistance(Long overDistance) {
        return (int) ((((overDistance - 1) / 5) + 1) * 100);
    }

    private static int calculateOverFareWhenExceedUpperDistance(Long overDistance) {
        return (int) ((((overDistance - 1) / 8) + 1) * 100);
    }
}
