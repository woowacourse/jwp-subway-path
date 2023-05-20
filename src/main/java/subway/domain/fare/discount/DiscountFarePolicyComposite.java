package subway.domain.fare.discount;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.fare.DiscountFare;
import subway.domain.fare.Fare;

public class DiscountFarePolicyComposite {

    private final List<DiscountFarePolicy> discountFarePolicies;

    public DiscountFarePolicyComposite(final List<DiscountFarePolicy> discountFarePolicies) {
        this.discountFarePolicies = discountFarePolicies;
    }

    public DiscountFare getDiscountFares(final Fare totalFare) {
        final List<Fare> fares = discountFarePolicies.stream()
            .map(policy -> policy.calculateFare(totalFare))
            .collect(Collectors.toUnmodifiableList());
        return new DiscountFare(fares.get(0), fares.get(1));
    }
}
