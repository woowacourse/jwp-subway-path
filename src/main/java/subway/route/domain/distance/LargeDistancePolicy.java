package subway.route.domain.distance;

public class LargeDistancePolicy implements DistanceFareStrategy {

    private static final long BASE_FARE = 1250L + 800L;
    private static final int MINIMUM_DISTANCE = 50;

    @Override
    public long calculateFare(long distance) {
        long removeFiftyKM = distanceForCalculation(distance);
        return BASE_FARE + (long) (Math.ceil((removeFiftyKM + 7) / 8) * 100);
    }

    private long distanceForCalculation(long distance) {
        return distance - MINIMUM_DISTANCE;
    }
}
