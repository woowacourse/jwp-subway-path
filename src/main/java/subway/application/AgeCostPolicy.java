package subway.application;

import subway.domain.Path;

public class AgeCostPolicy implements CostPolicy {

    @Override
    public long calculate(final Path path, final long cost) {
        final AgeCost ageCost = AgeCost.findByAge(path.getAge());
        return ageCost.calculateCost(cost);
    }
}
