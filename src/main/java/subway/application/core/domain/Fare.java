package subway.application.core.domain;

import subway.application.core.exception.FareCantCalculatedException;

import java.util.Arrays;

public enum Fare {

    UNDER_TEN_KM(0.0, 10.0) {
        @Override
        int calculateFare(double distance) {
            return BASE_FARE;
        }
    },
    BETWEEN_TEN_AND_FIFTY(10.0, 50.0) {
        @Override
        int calculateFare(double distance) {
            distance -= 10;
            return BASE_FARE + calculateOverFareForEveryDistance(distance, REFERENCE_DISTANCE_UNDER_FIFTY);
        }
    },
    OVER_FIFTY(50.0, Integer.MAX_VALUE) {
        @Override
        int calculateFare(double distance) {
            distance -= 50;
            return BASE_FARE + calculateOverFareForEveryDistance(40, REFERENCE_DISTANCE_UNDER_FIFTY) +
                    calculateOverFareForEveryDistance(distance, REFERENCE_DISTANCE_OVER_FIFTY);
        }
    };

    private static final int BASE_FARE = 1_250;
    private static final double REFERENCE_DISTANCE_UNDER_FIFTY = 5.0;
    private static final double REFERENCE_DISTANCE_OVER_FIFTY = 8.0;

    protected final double startPoint;
    protected final double endPoint;

    Fare(double startPoint, double endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public static int of(double distance) {
        return Arrays.stream(values())
                .filter(fareEnum -> fareEnum.matches(distance))
                .findAny()
                .orElseThrow(FareCantCalculatedException::new)
                .calculateFare(distance);
    }

    abstract int calculateFare(double distance);
    
    protected int calculateOverFareForEveryDistance(double distance, double referenceDistance) {
        return (int) ((Math.ceil(((int) distance - 1) / (int) referenceDistance) + 1) * 100);
    }

    protected boolean matches(double distance) {
        return Double.compare(startPoint, distance) <= 0 && Double.compare(distance, endPoint) <= 0;
    }
}
