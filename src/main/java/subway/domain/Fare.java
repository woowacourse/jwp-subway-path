package subway.domain;

import subway.exception.ErrorCode;
import subway.exception.InvalidException;

public class Fare {
    public static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_FARE_OVER_50KM = 2050;
    public static final int UNIT_OVER_10KM = 5;
    public static final int UNIT_OVER_50KM = 8;
    public static final int ADD_FARE = 100;

    public static int calculateFare(int distance) {
        validateDistance(distance);
        int fare = DEFAULT_FARE;

        if (distance > 50) {
            fare = DEFAULT_FARE_OVER_50KM;
            fare += calculateOverFare(distance - 50, UNIT_OVER_50KM);
        }

        if (distance >= 10 && distance <= 50) {
            fare += calculateOverFare(distance - 10, UNIT_OVER_10KM);
        }

        return fare;
    }

    private static void validateDistance(int distance) {
        if (distance <= 0) {
            throw new InvalidException(ErrorCode.INVALID_NOT_POSITIVE_DISTANCE);
        }
    }

    private static int calculateOverFare(int distance, int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * ADD_FARE);
    }
}
