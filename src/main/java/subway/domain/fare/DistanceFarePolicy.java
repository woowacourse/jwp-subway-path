package subway.domain.fare;

import subway.domain.path.Path;

import java.util.ArrayList;

public class DistanceFarePolicy implements FarePolicy {

    private static final int ZERO = 0;
    public static final int FIRST_DISTANCE_THRESHOLD = 10;
    public static final int SECOND_DISTANCE_THRESHOLD = 50;
    public static final int FIRST_DISTANCE_THRESHOLD_FARE_INCREMENT = 5;
    public static final int SECOND_DISTANCE_THRESHOLD_FARE_INCREMENT = 8;
    public static final int ADDITIONAL_FARE = 100;

    @Override
    public int calculateFare(Path path) {
        int distance = path.getDistance();

        if (distance > SECOND_DISTANCE_THRESHOLD) {
            Path secondDistanceThresholdPath = new Path(new ArrayList<>(), new ArrayList<>(), SECOND_DISTANCE_THRESHOLD);
            return calculateFare(secondDistanceThresholdPath) +
                    (int) Math.ceil((double) (distance - SECOND_DISTANCE_THRESHOLD) / SECOND_DISTANCE_THRESHOLD_FARE_INCREMENT) * ADDITIONAL_FARE;
        }
        if (distance > FIRST_DISTANCE_THRESHOLD) {
            return (int) Math.ceil((double) (distance - FIRST_DISTANCE_THRESHOLD) / FIRST_DISTANCE_THRESHOLD_FARE_INCREMENT) * ADDITIONAL_FARE;
        }
        return ZERO;
    }
}
