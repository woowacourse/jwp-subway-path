package subway.application.costPolicy;

import subway.domain.Path;

public class AgeCostPolicyChain implements CostPolicyChain {

    private CostPolicyChain next;

    @Override
    public void setNext(final CostPolicyChain next) {
        this.next = next;
    }

    @Override
    public long calculate(final Path path, final int age, final long cost) {
        final AgeCost ageCost = AgeCost.findByAge(age);
        return calculateNext(path, age, ageCost.calculateCost(cost));
    }

    private long calculateNext(final Path path, final int age, final long cost) {
        if (next == null) {
            return cost;
        }
        return next.calculate(path, age, cost);
    }
}
