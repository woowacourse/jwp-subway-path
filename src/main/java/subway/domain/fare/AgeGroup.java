package subway.domain.fare;

import java.util.Arrays;

public enum AgeGroup {

    ADULT(20, Integer.MAX_VALUE, 0, 0),
    YOUTH(13, 18, 350, 0.2),
    CHILD(6, 12, 350, 0.5),
    PRESCHOOLERS(0, 5, 0, 1);

    private final int minAge;
    private final int maxAge;
    private final int deductionAmount;
    private final double discountRate;

    AgeGroup(final int minAge, final int maxAge, final int deductionAmount, final double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deductionAmount = deductionAmount;
        this.discountRate = discountRate;
    }

    public static AgeGroup from(int age) {
        return Arrays.stream(values())
                .filter(ageGroup -> ageGroup.minAge <= age && age <= ageGroup.maxAge)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("포함되지 않은 나이입니다."));
    }

    public int calculate(int fare) {
        fare -= deductionAmount;
        return (int) (fare * (1 - discountRate));
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getDeductionAmount() {
        return deductionAmount;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
