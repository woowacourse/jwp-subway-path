package subway.domain.fare;

import org.springframework.stereotype.Component;
import subway.domain.path.PathEdgeProxy;

import java.util.List;

@Component
public final class FareCalculator {
    private final DistanceFareStrategy distanceFareStrategy;
    private final LineAdditionalFareStrategy lineAdditionalFareStrategy;
    private final AgeFareStrategy ageFareStrategy;

    public FareCalculator(final DistanceFareStrategy distanceFareStrategy, final LineAdditionalFareStrategy lineAdditionalFareStrategy, final AgeFareStrategy ageFareStrategy) {
        this.distanceFareStrategy = distanceFareStrategy;
        this.lineAdditionalFareStrategy = lineAdditionalFareStrategy;
        this.ageFareStrategy = ageFareStrategy;
    }

    public int of(final List<PathEdgeProxy> shortest, final int age) {
        final int distance = shortest.stream()
                .mapToInt(PathEdgeProxy::getDistance)
                .sum();

        final int fare = distanceFareStrategy.calculate(distance) +
                lineAdditionalFareStrategy.calculate(shortest);
        return ageFareStrategy.calculate(age, fare);
    }
}
