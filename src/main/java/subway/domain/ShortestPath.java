package subway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ShortestPath {
    private static final double BASE_DISTANCE = 10;
    private static final double ADDITIONAL_FARE_DISTANCE = 50;
    private static final int FIRST_ADDITIONAL_DISTANCE_INTERVAL = 5;
    private static final int SECOND_ADDITIONAL_DISTANCE_INTERVAL = 8;
    private static final int ADDITIONAL_FARE = 100;
    private static final int BASIC_FARE = 1250;

    private final List<Section> path;
    private final double distance;
    private final int fare;

    public static ShortestPath of(List<Section> path, double distance) {
        int fare = calculateFare(distance);
        return new ShortestPath(path, distance, fare);
    }

    private static int calculateFare(double distance) {
        int additionalFare = 0;
        if (distance > BASE_DISTANCE && distance <= ADDITIONAL_FARE_DISTANCE) {
            additionalFare = (int) ((Math.ceil((distance - BASE_DISTANCE) / FIRST_ADDITIONAL_DISTANCE_INTERVAL) * ADDITIONAL_FARE));
        }
        if (distance > ADDITIONAL_FARE_DISTANCE) {
            additionalFare = (800 + (int) ((Math.ceil((distance - ADDITIONAL_FARE_DISTANCE)
                    / SECOND_ADDITIONAL_DISTANCE_INTERVAL) * ADDITIONAL_FARE)));
        }
        return BASIC_FARE + additionalFare;
    }
}
