package subway.route.domain.distance;

public class MiddleDistancePolicy implements DistanceFareStrategy {

    private static final long BASE_FARE = 1250L;
    private static final long MAXIMUM_DISTANCE = 50L;
    private static final long MINIMUM_DISTANCE = 10L;
    private static final int AMOUNT_PER_UNIT = 100;
    private static final int UNIT_SIZE = 5;

    @Override
    public long calculateFare(long distance) {
        long removeTenKM = distanceForCalculation(distance);
        long unit = (removeTenKM + UNIT_SIZE - 1) / UNIT_SIZE;
        return BASE_FARE + unit * AMOUNT_PER_UNIT;
    }

    private long distanceForCalculation(long distance) {
        if (distance > MAXIMUM_DISTANCE) {
            return MAXIMUM_DISTANCE - MINIMUM_DISTANCE;
        }
        return distance - MINIMUM_DISTANCE;
    }
}
