package subway.service.domain;

import java.util.Arrays;

public enum Age {

    CHILD(6, 13, 350, 0.5),
    YOUTH(13, 19, 350, 0.2),
    ADULT(19, Integer.MAX_VALUE, 0, 0.0);

    private final Integer ageLowerBoundInclusive;
    private final Integer ageUpperBoundExclusive;
    private final Integer amountDeducted;
    private final Double discountRate;

    Age(Integer ageLowerBoundInclusive, Integer ageUpperBoundExclusive, Integer amountDeducted, Double discountRate) {
        this.ageLowerBoundInclusive = ageLowerBoundInclusive;
        this.ageUpperBoundExclusive = ageUpperBoundExclusive;
        this.amountDeducted = amountDeducted;
        this.discountRate = discountRate;
    }

    public static Age from(Integer age) {
        return Arrays.stream(values())
                .filter(element -> isSameAgeGroup(age, element))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("아직 지하철을 탈 수 없습니다."));
    }

    private static boolean isSameAgeGroup(Integer age, Age element) {
        return element.ageLowerBoundInclusive <= age && age < element.ageUpperBoundExclusive;
    }

    public Integer getAmountDeducted() {
        return amountDeducted;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

}
