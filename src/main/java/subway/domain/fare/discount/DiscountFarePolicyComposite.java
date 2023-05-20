package subway.domain.fare.discount;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.fare.Fare;

public class DiscountFarePolicyComposite {
    private final List<DiscountFarePolicy> discountFarePolicies;

    public DiscountFarePolicyComposite(final List<DiscountFarePolicy> discountFarePolicies) {
        this.discountFarePolicies = discountFarePolicies;
    }

    public List<Fare> getDiscountFares(final Fare totalFare) {
        return discountFarePolicies.stream()
            .map(policy -> policy.calculateFare(totalFare))
            .collect(Collectors.toUnmodifiableList());
    }
}
