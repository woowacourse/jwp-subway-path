package subway.domain.fare;

import org.springframework.stereotype.Component;
import subway.domain.path.PathEdgeProxy;

import java.util.List;

@Component
public final class FareCalculator {
    private final DistanceFareStrategy distanceFareStrategy;
    private final LineAdditionalFareStrategy lineAdditionalFareStrategy;

    public FareCalculator(final DistanceFareStrategy distanceFareStrategy, final LineAdditionalFareStrategy lineAdditionalFareStrategy) {
        this.distanceFareStrategy = distanceFareStrategy;
        this.lineAdditionalFareStrategy = lineAdditionalFareStrategy;
    }

    public int of(final List<PathEdgeProxy> shortest) {
        final int distance = shortest.stream()
                .mapToInt(PathEdgeProxy::getDistance)
                .sum();

        return distanceFareStrategy.calculate(distance) + lineAdditionalFareStrategy.calculate(shortest);
    }
}
