package subway.application.costpolicy;

import java.util.Arrays;
import subway.domain.vo.Age;

public enum AgeCost {

    CHILD(Age.from(6), Age.from(12), 350L, 0.2D),
    TEENAGER(Age.from(13), Age.from(18), 350L, 0.5D),
    NOTHING(Age.from(1), Age.from(Integer.MAX_VALUE), 0L, 0D);

    private final Age minAge;
    private final Age maxAge;
    private final long deductCost;
    private final double discountRate;

    AgeCost(final Age minAge, final Age maxAge, final long deductCost, final double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deductCost = deductCost;
        this.discountRate = discountRate;
    }

    public static AgeCost findByAge(final Age age) {
        return Arrays.stream(values())
            .filter(it -> age.isLessThan(it.maxAge) && age.isMoreThan(it.minAge))
            .findAny()
            .orElse(NOTHING);
    }

    public long calculateCost(final long cost) {
        final long deductedCost = cost - deductCost;
        return (long) (deductedCost * (1.0D - discountRate));
    }
}
