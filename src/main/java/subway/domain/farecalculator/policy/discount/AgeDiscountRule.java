package subway.domain.farecalculator.policy.discount;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import subway.dto.FareResponse;

public enum AgeDiscountRule {
    DEFAULT(null, null, 0, 1.0),
    YOUTH(6, 13, 350, 0.8),
    CHILD(13, 19, 350, 0.5);

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

    public static List<FareResponse> computeFares(Integer fare) {
        return Arrays.stream(AgeDiscountRule.values()).
                map(rule -> new FareResponse(rule.name(), rule.discount(fare)))
                .collect(Collectors.toUnmodifiableList());
    }
}
