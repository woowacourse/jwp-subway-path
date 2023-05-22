package subway.application.costpolicy;

import subway.domain.Path;
import subway.domain.vo.Age;

public class LineCostPolicyChain implements CostPolicyChain {

    private CostPolicyChain next;

    @Override
    public void setNext(final CostPolicyChain next) {
        this.next = next;
    }

    @Override
    public long calculate(final Path path, final Age age, final long cost) {
        final long highestLineCharge = path.findHighestLineCharge();
        return calculateNext(path, age, cost + highestLineCharge);
    }

    private long calculateNext(final Path path, final Age age, final long cost) {
        if (next == null) {
            return cost;
        }
        return next.calculate(path, age, cost);
    }
}
