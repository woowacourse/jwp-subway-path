package subway.domain;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Primary
@Component
public class PricePolicyComposite implements SubwayPricePolicy {

    private final List<SubwayPricePolicy> policies;

    public PricePolicyComposite(final List<SubwayPricePolicy> policies) {
        this.policies = policies;
    }

    @Override
    public int calculate(final Route route) {
        return policies.stream()
                       .mapToInt(it -> it.calculate(route))
                       .sum();
    }
}
