package subway.application.costpolicy;

import org.springframework.stereotype.Component;
import subway.domain.Path;

@Component
public class DistanceCostPolicyChain implements CostPolicyChain {

    private CostPolicyChain next;

    @Override
    public void setNext(final CostPolicyChain next) {
        this.next = next;
    }

    @Override
    public long calculate(final Path path, final int age, final long cost) {
        final long calculateAdditionalCost = DistanceCost.calculateAdditionalCost(cost, path.getDistance());
        return calculateNext(path, age, calculateAdditionalCost);
    }

    private long calculateNext(final Path path, final int age, final long cost) {
        if (next == null) {
            return cost;
        }
        return next.calculate(path, age, cost);
    }
}
