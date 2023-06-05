package subway.route.domain;

import subway.route.domain.distance.DistanceFareStrategy;
import subway.route.domain.distance.DistancePolicy;

public class FareCalculator {

    public long calculateFare(int distance) {
        DistanceFareStrategy fareStrategy = DistancePolicy.from(distance);
        return fareStrategy.calculateFare(distance);
    }
}
