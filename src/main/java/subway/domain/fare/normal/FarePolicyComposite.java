package subway.domain.fare.normal;

import java.util.List;
import subway.domain.fare.Fare;
import subway.domain.route.Distance;

public class FarePolicyComposite {

    private final List<FarePolicy> farePolicies;

    public FarePolicyComposite(final List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    public Fare getTotalFare(final Distance shortestDistance) {
        return farePolicies.stream()
            .filter(policy -> policy.isAvailable(shortestDistance))
            .map(policy -> policy.calculateFare(shortestDistance))
            .findFirst()
            .orElseThrow();
    }
}
