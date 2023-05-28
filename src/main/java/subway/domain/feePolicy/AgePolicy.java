package subway.domain.feePolicy;

import java.util.Arrays;

public enum AgePolicy {

    DEFAULT(0, 0, 0, 0),
    KIDS(6, 12, 350, 0.5),
    YOUTH(13, 18, 350, 0.2);

    private final int minAge;
    private final int maxAge;
    private final int fixedDiscount;
    private final double discountPercent;

    AgePolicy(final int minAge, final int maxAge, final int fixedDiscount, final double discountPercent) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.fixedDiscount = fixedDiscount;
        this.discountPercent = discountPercent;
    }

    public static AgePolicy from(final int age) {
        return Arrays.stream(AgePolicy.values())
                .filter(agePolicy -> agePolicy.minAge <= age && age <= agePolicy.maxAge)
                .findFirst()
                .orElse(DEFAULT);
    }

    public int discount(final int fee) {
        if (this == DEFAULT) {
            return fee;
        }

        int result = fee - fixedDiscount;

        return applyPercentage(result);
    }

    private int applyPercentage(final int result) {
        return (int) Math.ceil(result - (result * discountPercent));
    }
}
