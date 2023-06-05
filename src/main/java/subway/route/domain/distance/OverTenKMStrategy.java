package subway.route.domain.distance;

public class OverTenKMStrategy implements DistanceFareStrategy {

    private static final int MAXIMUM_DISTANCE = 50;
    private static final int MINIMUM_DISTANCE = 10;

    @Override
    public long calculateFare(final long distance) {
        final long removeTenKM = distanceForCalculation(distance);
        return (long) (Math.ceil((removeTenKM + 4) / 5) * 100);
    }

    private long distanceForCalculation(final long distance) {
        if (distance > MAXIMUM_DISTANCE) {
            return MAXIMUM_DISTANCE - MINIMUM_DISTANCE;
        }
        return distance - MINIMUM_DISTANCE;
    }
}
