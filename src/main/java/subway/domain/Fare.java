package subway.domain;

import subway.exception.ErrorCode;
import subway.exception.InvalidException;

public class Fare {
    public static final int DEFAULT_FARE = 1250;
    public static final int UNIT_OVER_TEN = 5;
    public static final int UNIT_OVER_FIFTY = 8;
    public static final int ADD_FARE = 100;

    private int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public Fare() {
        this(DEFAULT_FARE);
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new InvalidException(ErrorCode.INVALID_NOT_POSITIVE_DISTANCE);
        }
    }

    public int calculateFare(int distance) {
        validateDistance(distance);
        fare += (calculateOverFare(distance - 10, UNIT_OVER_TEN) - calculateOverFare(distance - 50, UNIT_OVER_TEN));
        fare += calculateOverFare(distance - 50, UNIT_OVER_FIFTY);

        return fare;
    }

    private int calculateOverFare(int distance, int unit) {
        if (distance < 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * ADD_FARE);
    }
}
