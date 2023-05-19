package subway.domain.fare;

import java.util.function.Function;

public enum DistancePolicy {

    BASE_FIFTY(50, distance -> {
        final long additionDistance = distance - 50;
        return calculateDistanceFarePerStep(additionDistance, 8);
    }),
    BASE_TEN(10, distance -> {
        final long additionDistance = Math.min(BASE_FIFTY.base, distance) - 10;
        return calculateDistanceFarePerStep(additionDistance, 5);
    });

    private final int base;
    private final Function<Long, Long> policy;

    DistancePolicy(final int base, final Function<Long, Long> policy) {
        this.base = base;
        this.policy = policy;
    }

    private static long calculateDistanceFarePerStep(final long distance, final int step) {
        if (distance <= 0) {
            return 0L;
        }
        return (long) ((Math.ceil((distance - 1) / step) + 1) * 100);
    }

    public long calculateAdditionFare(final long distance) {
        return policy.apply(distance);
    }
}
