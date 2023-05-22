package subway.application.costPolicy;

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
        final long distance = path.getDistance().getValue();
        if (distance <= 10) {
            return calculateNext(path, age, cost);
        }

        // 10 ~ 50
        final long additionalDistanceUnder50 = Math.min(distance, 50) - 10L;
        long changeCost = cost + (long) ((Math.ceil((double) additionalDistanceUnder50 / 5)) * 100);

        // 50 초과
        if (distance > 50) {
            final long additionalDistanceOver50 = distance - 50;
            changeCost += (long) ((Math.ceil((double) additionalDistanceOver50 / 8)) * 100);
        }

        return calculateNext(path, age, changeCost);
    }

    private long calculateNext(final Path path, final int age, final long cost) {
        if (next == null) {
            return cost;
        }
        return next.calculate(path, age, cost);
    }
}
