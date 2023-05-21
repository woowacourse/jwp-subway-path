package subway.domain.fare;

import subway.domain.fare.policy.DiscountPolicy;
import subway.domain.fare.policy.FarePolicy;

public class FareCalculator {

    private final FarePolicy farePolicy;
    private final DiscountPolicy discountPolicy;

    public FareCalculator(final FarePolicy farePolicy, final DiscountPolicy discountPolicy) {
        this.farePolicy = farePolicy;
        this.discountPolicy = discountPolicy;
    }

    public int calculate(final FareInformation fareInformation) {
        int fare = farePolicy.calculate(fareInformation);
        return discountPolicy.calculate(fare, fareInformation);
    }
}
