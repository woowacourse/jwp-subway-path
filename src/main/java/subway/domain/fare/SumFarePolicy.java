package subway.domain.fare;

import java.util.List;
import subway.domain.route.Route;

public class SumFarePolicy implements FarePolicy {

    private final List<FarePolicy> policies;

    public SumFarePolicy(final List<FarePolicy> policies) {
        this.policies = policies;
    }

    @Override
    public Fare calculate(final Route route) {
        Fare totalFare = new Fare();
        for (final FarePolicy policy : policies) {
            totalFare = totalFare.plus(policy.calculate(route));
        }
        return totalFare;
    }
}
