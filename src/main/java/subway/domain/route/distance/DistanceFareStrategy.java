package subway.domain.route.distance;

public interface DistanceFareStrategy {

    long calculateFare(final long distance);

    default DistanceFareStrategy add(final DistanceFareStrategy distanceFareStrategy) {
        return (final long distance) -> calculateFare(distance) + distanceFareStrategy.calculateFare(distance);
    }
}
