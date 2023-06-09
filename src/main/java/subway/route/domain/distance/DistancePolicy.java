package subway.route.domain.distance;

import java.util.Arrays;
import java.util.function.LongPredicate;

public enum DistancePolicy {
    DEFAULT_POLICY((long distance) -> distance <= 10, new DefaultDistancePolicy()),
    MIDDLE_DISTANCE_POLICY((long distance) -> distance > 10 && distance <= 50, new MiddleDistancePolicy()),
    LARGE_DISTANCE_POLICY((long distance) -> distance > 50, new LargeDistancePolicy());

    private final LongPredicate distancePredicate;
    private final DistanceFareStrategy distanceFareStrategy;

    DistancePolicy(LongPredicate distancePredicate, DistanceFareStrategy distanceFareStrategy) {
        this.distancePredicate = distancePredicate;
        this.distanceFareStrategy = distanceFareStrategy;
    }

    public static DistancePolicy from(long distance) {
        return Arrays.stream(values())
                .filter(distancePolicy -> distancePolicy.distancePredicate.test(distance))
                .findFirst()
                .orElseThrow(InvalidDistanceException::new);
    }

    public long calculateFare(long distance) {
        return distanceFareStrategy.calculateFare(distance);
    }
}
