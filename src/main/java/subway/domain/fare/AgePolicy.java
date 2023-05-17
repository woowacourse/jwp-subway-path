package subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import subway.exception.InvalidPolicyException;

public enum AgePolicy {

    BABY(age -> age < 6, fare -> 0d),
    KID(age -> 6 <= age && age < 13, fare -> fare * 0.5),
    TEEN(age -> 13 <= age && age < 19, fare -> fare * 0.2),
    ADULT(age -> age >= 19, fare -> 0d);

    private final Function<Integer, Boolean> validTarget;
    private final Function<Double, Double> discount;

    AgePolicy(final Function<Integer, Boolean> validTarget, final Function<Double, Double> discount) {
        this.validTarget = validTarget;
        this.discount = discount;
    }

    public static AgePolicy search(final int age) {
        return Arrays.stream(AgePolicy.values())
                .filter(agePolicy -> agePolicy.canBeApplied(age))
                .findFirst()
                .orElseThrow(() -> new InvalidPolicyException("적용할 수 있는 정책이 존재하지 않습니다."));
    }

    private boolean canBeApplied(final int age) {
        return validTarget.apply(age);
    }

    public double calculateDiscountFare(final double fare) {
        return discount.apply(fare);
    }
}
