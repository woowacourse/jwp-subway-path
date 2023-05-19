package subway.domain.route.distance;

public class OverTenKMStrategy implements DistanceFareStrategy {

    @Override
    public long calculateFare(final long distance) {
        final long removeTenKM = distance - 10;
        return (long) (Math.ceil((removeTenKM + 4) / 5) * 100);
    }
}
