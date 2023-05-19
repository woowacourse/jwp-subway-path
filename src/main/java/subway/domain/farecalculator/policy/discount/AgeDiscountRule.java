package subway.domain.farecalculator.policy.discount;

import java.util.Arrays;

import subway.dto.FareResponse;

public enum AgeDiscountRule {
    DEFAULT(null, null, 0, 1.0),
    YOUTH(13, 19, 350, 0.8),
    CHILD(6, 13, 350, 0.5);

    private final Integer startAge;
    private final Integer endAge;
    private final Integer flatDiscount;
    private final Double discountRate;

    AgeDiscountRule(Integer startAge, Integer endAge, Integer flatDiscount, Double discountRate) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.flatDiscount = flatDiscount;
        this.discountRate = discountRate;
    }

    public Integer discount(Integer fare) {
        return (int) ((fare - flatDiscount) * discountRate);
    }

    public boolean isInRange(Integer age) {
        return startAge <= age && age < endAge;
    }
    public static FareResponse computeFare(Integer age, Integer fare) {
        final AgeDiscountRule ageDiscountRule = find(age);
        return new FareResponse(ageDiscountRule.name(), ageDiscountRule.discount(fare));
    }

    private static AgeDiscountRule find(Integer age) {
        return Arrays.stream(AgeDiscountRule.values())
                .filter(ageDiscountRule -> ageDiscountRule != DEFAULT)
                .filter(ageDiscountRule -> ageDiscountRule.isInRange(age))
                .findFirst()
                .orElse(DEFAULT);
    }
}
