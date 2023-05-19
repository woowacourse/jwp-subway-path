package subway.domain;

import java.util.Arrays;
import java.util.function.Function;

import subway.domain.exception.NoSuchFarePolicyException;

public enum FarePolicy {
    DEFAULT(0, 10, 1250, extraDistance -> 0),
    MIDDLE_DISTANCE(10, 50, DEFAULT.baseFare, extraDistance -> calculateExtraFareFor(extraDistance, 5)),
    LONG_DISTANCE(50, Integer.MAX_VALUE, 2050, extraDistance -> calculateExtraFareFor(extraDistance, 8));

    private static final int EXTRA_FARE_PER_UNIT = 100;

    private final int from;
    private final int to;
    private final int baseFare;
    private final Function<Integer, Integer> extraFare;

    FarePolicy(int from, int to, int baseFare, Function<Integer, Integer> extraFare) {
        this.from = from;
        this.to = to;
        this.baseFare = baseFare;
        this.extraFare = extraFare;
    }

    public int calculate(int distance) {
        return baseFare + extraFare.apply(distance - from);
    }

    public static FarePolicy of(int distance) {
        return Arrays.stream(values())
                .filter(policy -> policy.coversRangeOf(distance))
                .findFirst()
                .orElseThrow(NoSuchFarePolicyException::new);
    }

    private boolean coversRangeOf(int distance) {
        return from < distance && distance <= to;
    }

    private static int calculateExtraFareFor(int extraDistance, int unit) {
        return ((int)(Math.ceil(extraDistance - 1) / unit) + 1) * EXTRA_FARE_PER_UNIT;
    }
}
