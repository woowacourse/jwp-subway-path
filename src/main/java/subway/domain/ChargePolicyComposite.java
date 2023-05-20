package subway.domain;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Primary
@Component
public class ChargePolicyComposite implements SubwayChargePolicy {

    private final List<SubwayChargePolicy> policies;

    public ChargePolicyComposite(final List<SubwayChargePolicy> policies) {
        this.policies = policies;
    }

    @Override
    public int calculate(final Route route) {
        return policies.stream()
                       .mapToInt(it -> it.calculate(route))
                       .sum();
    }
}
