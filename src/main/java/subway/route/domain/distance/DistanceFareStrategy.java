package subway.route.domain.distance;

public interface DistanceFareStrategy {

    long calculateFare(long distance);

    default DistanceFareStrategy add(DistanceFareStrategy distanceFareStrategy) {
        return (final long distance) -> calculateFare(distance) + distanceFareStrategy.calculateFare(distance);
    }
}
