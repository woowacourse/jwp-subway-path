package subway.domain.subway.billing_policy;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.Path;

@Component
public final class BillingPolices {

    private final List<BillingPolicy> billingPolicies;

    public BillingPolices(final List<BillingPolicy> billingPolicies) {
        this.billingPolicies = billingPolicies;
    }

    public Fare calculateFare(final Path path) {
        return billingPolicies.stream()
                .map(billingPolicy -> billingPolicy.calculateFare(path))
                .reduce(new Fare(0), Fare::add);
    }
}
