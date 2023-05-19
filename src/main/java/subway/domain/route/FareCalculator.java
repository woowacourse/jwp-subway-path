package subway.domain.route;

import subway.domain.route.distance.DistanceFareStrategy;
import subway.domain.route.distance.DistancePolicy;

public class FareCalculator {

    public long calculateFare(final int distance) {
        final DistanceFareStrategy fareStrategy = DistancePolicy.from(distance);
        return fareStrategy.calculateFare(distance);
    }
}
