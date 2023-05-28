package subway.domain;

import java.util.Arrays;
import java.util.Optional;

public enum DiscountPolicy {

    NONE(0, 0, 0, 0),
    KIDS(6, 12, 350, 0.5),
    YOUTH(13, 18, 350, 0.2);

    private final int minAge;
    private final int maxAge;
    private final int fixedDiscount;
    private final double discountPercent;

    DiscountPolicy(final int minAge, final int maxAge, final int fixedDiscount, final double discountPercent) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.fixedDiscount = fixedDiscount;
        this.discountPercent = discountPercent;
    }

    public static int discount(final int fee, final int age) {
        DiscountPolicy discountPolicy = from(age);
        if (discountPolicy == NONE) {
            return fee;
        }
        
        int result = fee - discountPolicy.fixedDiscount;

        return applyPercentage(discountPolicy, result);
    }

    private static DiscountPolicy from(final int age) {
        Optional<DiscountPolicy> filtered = Arrays.stream(DiscountPolicy.values())
                .filter(discountPolicy -> discountPolicy.minAge <= age && age <= discountPolicy.maxAge)
                .findFirst();

        return filtered.orElse(NONE);
    }

    private static int applyPercentage(final DiscountPolicy discountPolicy, final int result) {
        return (int) Math.ceil(result - (result * discountPolicy.discountPercent));
    }

    public int getFixedDiscount() {
        return fixedDiscount;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }
}
