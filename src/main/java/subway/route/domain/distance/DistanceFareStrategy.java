package subway.route.domain.distance;

public interface DistanceFareStrategy {

    long calculateFare(long distance);
}
