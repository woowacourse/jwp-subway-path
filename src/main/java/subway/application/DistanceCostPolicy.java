package subway.application;

import org.springframework.stereotype.Component;
import subway.domain.Path;

@Component
public class DistanceCostPolicy implements CostPolicy {

    @Override
    public long calculate(final Path path, final long cost) {
        final long distance = path.getDistance().getValue();
        if (distance <= 10) {
            return cost;
        }

        // 10 ~ 50
        final long additionalDistanceUnder50 = Math.min(distance, 50) - 10L;
        long changeCost = cost + (long) ((Math.ceil((double) additionalDistanceUnder50 / 5)) * 100);

        // 50 초과
        if (distance > 50) {
            final long additionalDistanceOver50 = distance - 50;
            changeCost += (long) ((Math.ceil((double) additionalDistanceOver50 / 8)) * 100);
        }

        return changeCost;
    }
}
