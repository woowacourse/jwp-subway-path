package subway.application.costpolicy;

import subway.domain.Path;
import subway.domain.vo.Age;

public class AgeCostPolicyChain implements CostPolicyChain {

    private CostPolicyChain next;

    @Override
    public void setNext(final CostPolicyChain next) {
        this.next = next;
    }

    @Override
    public long calculate(final Path path, final Age age, final long cost) {
        final AgeCost ageCost = AgeCost.findByAge(age);
        return calculateNext(path, age, ageCost.calculateCost(cost));
    }

    private long calculateNext(final Path path, final Age age, final long cost) {
        if (next == null) {
            return cost;
        }
        return next.calculate(path, age, cost);
    }
}
