package subway.domain.subway.discount_policy;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.passenger.Passenger;
import subway.domain.subway.billing_policy.Fare;

@Component
public final class DiscountPolicies {

    private final List<DiscountPolicy> discountPolicies;

    public DiscountPolicies(final List<DiscountPolicy> discountPolicies) {
        this.discountPolicies = discountPolicies;
        sortPoliciesByPriority();
    }

    public Fare calculateDiscountedFare(Fare fare, Passenger passenger) {
        Fare discountedFare = fare;
        for (DiscountPolicy discountPolicy : discountPolicies) {
            discountedFare = discountPolicy.calculateDiscountedFare(discountedFare, passenger);
        }
        return discountedFare;
    }

    private void sortPoliciesByPriority() {
        discountPolicies.sort(
                (policy1, policy2) -> policy2.getPriority().getValue() - policy1.getPriority().getValue()
        );
    }
}
