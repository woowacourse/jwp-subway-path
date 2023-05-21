package subway.domain.common;

import subway.exception.InvalidDistanceException;

public class Fee {

    private static final int NOT_MOVE_DISTANCE = 0;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int MAX_MIDDLE_DISTANCE = 50;
    private static final int DEFAULT_FEE = 1250;
    private static final int MIDDLE_MAX_FEE = 2050;

    private int fee;

    private Fee(final int fee) {
        this.fee = fee;
    }

    public static Fee createDefault() {
        return new Fee(DEFAULT_FEE);
    }

    public void calculateFromDistance(final int distance) {
        validateDistance(distance);
        this.fee = calculateFee(distance);
    }

    private void validateDistance(final int distance) {
        if (distance == NOT_MOVE_DISTANCE) {
            throw new InvalidDistanceException();
        }
    }

    private int calculateFee(final int distance) {
        if (distance <= DEFAULT_DISTANCE) {
            return DEFAULT_FEE;
        }

        if (distance <= MAX_MIDDLE_DISTANCE) {
            return getMiddleDistanceFee(distance);
        }

        return getEndOfDistanceFee(distance);
    }

    private int getMiddleDistanceFee(final int distance) {
        int add = (int) ((Math.ceil((double) (distance - 10) / 5)) * 100);
        return DEFAULT_FEE + add;
    }

    private int getEndOfDistanceFee(final int distance) {
        return MIDDLE_MAX_FEE + (int) ((Math.ceil((double) (distance - 50) / 8)) * 100);
    }

    public int getFee() {
        return fee;
    }
}
