package subway.domain;

public class Fare {

    private static final int ADDITIONAL_MINIMUM_FARE = 2_050;
    private static final int MINIMUM_FARE = 1_250;

    private final int value;

    private Fare(final int value) {
        this.value = value;
    }

    public static Fare from(final int distance) {
        return new Fare(calculateFare(distance));
    }

    private static int calculateFare(int distance) {
        int additionalDistance = 0;
        if (distance <= 10) {
            return MINIMUM_FARE;
        }
        if (distance <= 50) {
            additionalDistance = distance - 10;
            return MINIMUM_FARE + calculateOverFare(additionalDistance);
        }
        additionalDistance = distance - 50;
        return ADDITIONAL_MINIMUM_FARE + calculateOverFare(additionalDistance);
    }

    private static int calculateOverFare(int addtionalDistance) {
        if (addtionalDistance <= 40) {
            return (int) ((Math.ceil((addtionalDistance - 1) / 5) + 1) * 100);
        }
        return (int) ((Math.ceil((addtionalDistance - 1) / 8) + 1) * 100);
    }

    public int getValue() {
        return value;
    }
}
