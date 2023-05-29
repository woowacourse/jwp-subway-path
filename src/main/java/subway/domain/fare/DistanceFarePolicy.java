package subway.domain.fare;

import subway.domain.line.Line;

import java.util.Set;

public class DistanceFarePolicy implements FarePolicy {

    private static final int ZERO = 0;
    public static final int FIRST_DISTANCE_THRESHOLD = 10;
    public static final int SECOND_DISTANCE_THRESHOLD = 50;
    public static final int FIRST_DISTANCE_THRESHOLD_FARE_INCREMENT = 5;
    public static final int SECOND_DISTANCE_THRESHOLD_FARE_INCREMENT = 8;
    public static final int ADDITIONAL_FARE = 100;

    @Override
    public int calculateFare(int distance, Set<Line> linesToUse) {
        if (distance > SECOND_DISTANCE_THRESHOLD) {
            return calculateFare(SECOND_DISTANCE_THRESHOLD, null) +
                    (int) Math.ceil((double) (distance - SECOND_DISTANCE_THRESHOLD) / SECOND_DISTANCE_THRESHOLD_FARE_INCREMENT) * ADDITIONAL_FARE;
        }
        if (distance > FIRST_DISTANCE_THRESHOLD) {
            return (int) Math.ceil((double) (distance - FIRST_DISTANCE_THRESHOLD) / FIRST_DISTANCE_THRESHOLD_FARE_INCREMENT) * ADDITIONAL_FARE;
        }
        return ZERO;
    }
}
