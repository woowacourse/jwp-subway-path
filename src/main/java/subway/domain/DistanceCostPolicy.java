package subway.domain;

public class DistanceCostPolicy implements CostPolicy {

    @Override
    public long calculate(final Path path) {
        final long distance = path.getDistance().getValue();
        if (distance <= 10) {
            return 1250L;
        }

        // 10 ~ 50
        final long additionalDistanceUnder50 = Math.min(distance, 50) - 10L;
        long cost = 1250L + (long) ((Math.ceil((double) additionalDistanceUnder50 / 5)) * 100);

        // 50 초과
        if (distance > 50) {
            final long additionalDistanceOver50 = distance - 50;
            cost += (long) ((Math.ceil((double) additionalDistanceOver50 / 8)) * 100);
        }

        return cost;
    }
}
