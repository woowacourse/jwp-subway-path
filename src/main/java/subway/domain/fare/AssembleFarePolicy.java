package subway.domain.fare;

import java.util.List;
import subway.domain.route.Route;

public class AssembleFarePolicy implements FarePolicy {

    private final List<FarePolicy> policies;

    public AssembleFarePolicy(final List<FarePolicy> policies) {
        this.policies = policies;
    }

    @Override
    public Fare calculate(final Route route, final Integer age, Fare fare) {
        for (final FarePolicy policy : policies) {
            fare = policy.calculate(route, age, fare);
        }
        return fare;
    }
}
