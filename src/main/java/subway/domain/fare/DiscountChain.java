package subway.domain.fare;

import java.util.List;

public class DiscountChain implements DistancePolicy {

    private final List<DistancePolicy> distanceChains;

    public DiscountChain(final List<DistancePolicy> distanceChains) {
        this.distanceChains = distanceChains;
    }

    @Override
    public int calculate(final int distance) {
        return distanceChains.stream()
                .mapToInt(distancePolicy -> distancePolicy.calculate(distance))
                .sum();
    }
}
