package subway.route.domain.distance;

public class LargeDistancePolicy implements DistanceFareStrategy {

    private static final long BASE_FARE = 1250L + 800L;
    private static final int MINIMUM_DISTANCE = 50;
    private static final int AMOUNT_PER_UNIT = 100;
    private static final int UNIT_SIZE = 8;

    @Override
    public long calculateFare(long distance) {
        long removeFiftyKM = distanceForCalculation(distance);
        long unit = (removeFiftyKM + UNIT_SIZE - 1) / UNIT_SIZE;
        return BASE_FARE + unit * AMOUNT_PER_UNIT;
    }

    private long distanceForCalculation(long distance) {
        return distance - MINIMUM_DISTANCE;
    }
}
