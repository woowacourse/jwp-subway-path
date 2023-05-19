package subway.domain.route.distance;

public class OverFiftyKMStrategy implements DistanceFareStrategy {

    @Override
    public long calculateFare(final long distance) {
        final long removeFiftyKM = distance - 50;
        return (long) (Math.ceil((removeFiftyKM + 7) / 8) * 100);
    }
}
