package subway.application.costPolicy;

import java.util.Arrays;

public enum AgeCost {

    CHILD(6, 12, 350L, 0.2D),
    TEENAGER(13, 18, 350L, 0.5D),
    NOTHING(Integer.MAX_VALUE, Integer.MIN_VALUE, 0L, 0D);

    private final int minAge;
    private final int maxAge;
    private final long deductCost;
    private final double discountRate;

    AgeCost(final int minAge, final int maxAge, final long deductCost, final double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deductCost = deductCost;
        this.discountRate = discountRate;
    }

    public static AgeCost findByAge(final int age) {
        return Arrays.stream(values())
            .filter(it -> it.minAge <= age && it.maxAge >= age)
            .findAny()
            .orElse(NOTHING);
    }

    public long calculateCost(final long cost) {
        final long deductedCost = cost - deductCost;
        return (long) (deductedCost * (1.0D - discountRate));
    }
}
