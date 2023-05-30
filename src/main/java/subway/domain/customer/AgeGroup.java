package subway.domain.customer;

import java.util.Arrays;

public enum AgeGroup {

    ADULT(19, 64, 0, 0),
    TEENAGER(13, 18, 350, 0.2),
    CHILD(6, 12, 350, 0.5),
    PREFERENTIAL(0, 1);

    private int minAge;
    private int maxAge;
    private final int deductPrice;
    private final double discountRate;

    AgeGroup(int deductPrice, double discountRate) {
        this.deductPrice = deductPrice;
        this.discountRate = discountRate;
    }

    AgeGroup(int minAge, int maxAge, int deductPrice, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deductPrice = deductPrice;
        this.discountRate = discountRate;
    }

    public static AgeGroup from(int age) {
        return Arrays.stream(values()).filter(ageGroup -> ageGroup != PREFERENTIAL)
                .filter(ageGroup -> ageGroup.minAge <= age && age <= ageGroup.maxAge)
                .findAny()
                .orElse(PREFERENTIAL);
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getDeductPrice() {
        return deductPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
