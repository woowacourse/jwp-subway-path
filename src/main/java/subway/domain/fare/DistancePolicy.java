package subway.domain.fare;

import java.util.function.Function;

public enum DistancePolicy {

    BASE_FIFTY(50, distance -> {
        final long additionDistance = distance - 50;
        if (additionDistance <= 0) {
            return 0L;
        }
        return (long) ((Math.ceil((additionDistance - 1) / 8) + 1) * 100);
    }),
    BASE_TEN(10, distance -> {
        final long additionDistance = Math.min(BASE_FIFTY.base, distance) - 10;
        if (additionDistance <= 0) {
            return 0L;
        }
        return (long) ((Math.ceil((additionDistance - 1) / 5) + 1) * 100);
    });

    private final int base;
    private final Function<Long, Long> policy;

    DistancePolicy(final int base, final Function<Long, Long> policy) {
        this.base = base;
        this.policy = policy;
    }

    public long calculateAdditionFare(final long distance) {
        return policy.apply(distance);
    }
}
