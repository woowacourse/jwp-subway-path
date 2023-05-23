package subway.domain;

public class FareCalculator {

    private static final int DEFAULT_FARE = 1_250;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int MIDDLE_DISTANCE = 50;

    public static int calculate(Double distance) {
        if (distance < DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }

        if (distance < MIDDLE_DISTANCE) {
            return DEFAULT_FARE + calculateMiddle(distance - DEFAULT_DISTANCE);
        }

        return DEFAULT_FARE + calculateMiddle((double) MIDDLE_DISTANCE - DEFAULT_DISTANCE) +
                calculateOver(distance - MIDDLE_DISTANCE);
    }

    private static int calculateMiddle(Double distance) {
        return (int) Math.ceil(distance / 5) * 100;
    }

    private static int calculateOver(Double distance) {
        return (int) Math.ceil(distance / 8) * 100;
    }
}
