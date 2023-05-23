package subway.domain.fare.discount;

import subway.domain.fare.FareInfo;
import subway.domain.fare.FarePolicy;

import java.util.List;

public class DiscountComposite {

    private final List<FarePolicy> discountPolicies;

    public DiscountComposite(final List<FarePolicy> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    public FareInfo doDiscount(final FareInfo fareInfo) {
        FareInfo currentFareInfo = fareInfo;
        for (final FarePolicy discountPolicy : discountPolicies) {
            currentFareInfo = discountPolicy.doCalculate(currentFareInfo);
        }
        return currentFareInfo;
    }
}
