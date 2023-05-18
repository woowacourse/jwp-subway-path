package subway.domain;

import java.util.function.Function;

public class Cost {
    private static final Distance DEFAULT_DISTANCE_STANDARD = new Distance(10);
    private static final Distance MIDDLE_DISTANCE_STANDARD = new Distance(50);
    private static final int DEFAULT_FARE = 1250;

    private static final Function<Distance, Integer> calculateOverCostByLongDistance =
            (distance) -> (int) ((Math.ceil((distance.getDistance() - 1) / 8) + 1) * 100);
    private static final Function<Distance, Integer> calculateOverCostByMiddleDistance =
            (distance) -> (int) ((Math.ceil((distance.getDistance() - 1) / 5) + 1) * 100);

    private final int fare;

    public Cost(Distance distance) {
        this.fare = calculateFare(distance);
    }

    private int calculateFare(Distance distance) {
        if (distance.isLessThanOrEqualTo(DEFAULT_DISTANCE_STANDARD)) {
            return DEFAULT_FARE;
        }

        if (distance.isLessThanOrEqualTo(MIDDLE_DISTANCE_STANDARD)) {
            return DEFAULT_FARE + calculateOverCostByMiddleDistance.apply(distance);
        }

        return DEFAULT_FARE + calculateOverCostByLongDistance.apply(distance);
    }

    public int getFare() {
        return fare;
    }
}
