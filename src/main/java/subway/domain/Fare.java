package subway.domain;

import java.util.Arrays;

public enum Fare {

    BASIC(1, 10, 10, 1250),
    MEDIUM_DISTANCE_ADDITION(11, 50, 5, 100),
    LONG_DISTANCE_ADDITION(51, Integer.MAX_VALUE, 8, 100);

    private final int lowerBoundDistance;
    private final int upperBoundDistance;
    private final int interval;
    private final int additionalCost;

    Fare(int lowerBoundDistance, int upperBoundDistance, int interval, int additionalCost) {
        this.lowerBoundDistance = lowerBoundDistance;
        this.upperBoundDistance = upperBoundDistance;
        this.interval = interval;
        this.additionalCost = additionalCost;
    }

    public static int getFare(int distance) {
        return Arrays.stream(values())
                .mapToInt(fare -> fare.calculateFare(distance))
                .sum();
    }

    private int calculateFare(int distance) {
        if (lowerBoundDistance > distance) {
            return 0;
        }
        int limitedDistance = Integer.min(upperBoundDistance, distance);
        return ((limitedDistance - lowerBoundDistance) / interval + 1) * additionalCost;
    }
}
