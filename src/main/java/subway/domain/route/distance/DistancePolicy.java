package subway.domain.route.distance;

import java.util.Arrays;
import java.util.function.LongPredicate;

public enum DistancePolicy {
    DEFAULT_POLICY((long distance) -> distance <= 10, new DefaultDistancePolicy().add(new OverTenKMStrategy())),

    OVER_10KM_POLICY((long distance) -> distance > 10 && distance <= 50,
            new DefaultDistancePolicy().add(new OverTenKMStrategy())),
    OVER_50KM_POLICY((long distance) -> distance > 50,
            new DefaultDistancePolicy().add(new OverFiftyKMStrategy()));

    private final LongPredicate distancePredicate;
    private final DistanceFareStrategy distanceFareStrategy;

    DistancePolicy(final LongPredicate distancePredicate, final DistanceFareStrategy distanceFareStrategy) {
        this.distancePredicate = distancePredicate;
        this.distanceFareStrategy = distanceFareStrategy;
    }

    public static DistanceFareStrategy of(final long distance) {
        return Arrays.stream(values())
                .filter(distancePolicy -> distancePolicy.distancePredicate.test(distance))
                .findFirst()
                .orElseThrow(InvalidDistanceException::new)
                .distanceFareStrategy;
    }
}
