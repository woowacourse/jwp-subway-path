package subway.route.domain.distance;

public class DefaultDistancePolicy implements DistanceFareStrategy {

    private static final long BASE_FARE = 1250;

    @Override
    public long calculateFare(final long distance) {
        return BASE_FARE;
    }
}
