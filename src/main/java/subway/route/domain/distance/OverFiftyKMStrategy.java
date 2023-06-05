package subway.route.domain.distance;

public class OverFiftyKMStrategy implements DistanceFareStrategy {

    private static final int MINIMUM_DISTANCE = 50;

    @Override
    public long calculateFare(final long distance) {
        final long removeFiftyKM = distanceForCalculation(distance);
        return (long) (Math.ceil((removeFiftyKM + 7) / 8) * 100);
    }

    private long distanceForCalculation(final long distance) {
        return distance - MINIMUM_DISTANCE;
    }
}
