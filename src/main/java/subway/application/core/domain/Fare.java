package subway.application.core.domain;

import subway.application.core.exception.DistanceNotPositiveException;

public class Fare {

    private static final int BASE_FARE = 1_250;
    private static final double BASE_DISTANCE = 10.0;
    private static final double REFERENCE_DISTANCE_UNDER_FIFTY = 5.0;
    private static final double REFERENCE_DISTANCE_OVER_FIFTY = 8.0;

    private final int value;

    public Fare(double distance) {
        validate(distance);
        this.value = chargeOf((int) distance);
    }

    private void validate(double distance) {
        if (distance < 0.0) {
            throw new DistanceNotPositiveException();
        }
    }

    private int chargeOf(double distance) {
        if (Double.compare(distance, BASE_DISTANCE) < 0) {
            return BASE_FARE;
        }
        return BASE_FARE + calculateOverFare(distance);
    }

    private int calculateOverFare(double distance) {
        if (isDistanceBetweenTenAndFifty(distance)) {
            distance -= 10.0;
            return calculateOverFareForEveryDistance(distance, REFERENCE_DISTANCE_UNDER_FIFTY);
        }
        distance -= 50.0;
        return calculateOverFareForEveryDistance(40, REFERENCE_DISTANCE_UNDER_FIFTY) +
                calculateOverFareForEveryDistance(distance, REFERENCE_DISTANCE_OVER_FIFTY);
    }

    private static boolean isDistanceBetweenTenAndFifty(double distance) {
        return Double.compare(distance, 10.0) >= 0 && Double.compare(distance, 50.0) <= 0;
    }

    private int calculateOverFareForEveryDistance(double distance, double referenceDistance) {
        return (int) ((Math.ceil(((int) distance - 1) / (int) referenceDistance) + 1) * 100);
    }

    public int value() {
        return value;
    }
}
