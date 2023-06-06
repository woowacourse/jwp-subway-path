package subway.route.domain.distance;

public class MiddleDistancePolicy implements DistanceFareStrategy {

    private static final long BASE_FARE = 1250L;
    private static final int MAXIMUM_DISTANCE = 50;
    private static final int MINIMUM_DISTANCE = 10;

    @Override
    public long calculateFare(long distance) {
        long removeTenKM = distanceForCalculation(distance);
        return (long) (BASE_FARE + (Math.ceil((removeTenKM + 4) / 5) * 100));
    }

    private long distanceForCalculation(long distance) {
        if (distance > MAXIMUM_DISTANCE) {
            return MAXIMUM_DISTANCE - MINIMUM_DISTANCE;
        }
        return distance - MINIMUM_DISTANCE;
    }
}
