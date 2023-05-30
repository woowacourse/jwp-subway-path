package subway.domain;

import java.util.function.Function;

public enum SubwayFare {

    BASIC(
            10,
            distance -> 1250
    ),
    EXTRA_ONE_STEP(
            50,
            distance -> 1250
                    + (int) Math.ceil((distance - 10) / 5.0) * 100
    ),
    EXTRA_TWO_STEP(
            Integer.MAX_VALUE,
            distance -> 1250 + (50 - 10) / 5 * 100
                    + (int) Math.ceil((distance - 50) / 8.0) * 100
    );


    private final int limit;
    private final Function<Integer, Integer> calculator;

    SubwayFare(final int limit, final Function<Integer, Integer> calculator) {
        this.limit = limit;
        this.calculator = calculator;
    }

    public static SubwayFare decideFare(final int distance) {
        if (distance <= BASIC.limit) {
            return BASIC;
        }
        if (distance <= EXTRA_ONE_STEP.limit) {
            return EXTRA_ONE_STEP;
        }
        return EXTRA_TWO_STEP;
    }

    public int calculateCharge(final int distance) {
        return this.calculator.apply(distance);
    }
}
