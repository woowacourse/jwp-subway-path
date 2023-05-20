package subway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ShortestPath {
    private static final Distance BASE_DISTANCE = new Distance(10);
    private static final Distance ADDITIONAL_FARE_DISTANCE = new Distance(50);
    private static final Distance FIRST_ADDITIONAL_DISTANCE_INTERVAL = new Distance(5);
    private static final Distance SECOND_ADDITIONAL_DISTANCE_INTERVAL = new Distance(8);
    private static final int ADDITIONAL_FARE = 100;
    private static final int BASIC_FARE = 1_250;

    private final List<Section> path;
    private final Distance distance;
    private final int fare;

    public static ShortestPath of(List<Section> path, Distance distance) {
        int fare = calculateFare(distance);
        return new ShortestPath(path, distance, fare);
    }

    private static int calculateFare(Distance distance) {
        int additionalFare = 0;
        if (distance.compareTo(BASE_DISTANCE) > 0 && distance.compareTo(ADDITIONAL_FARE_DISTANCE) != 1) {
            additionalFare = (int) ((Math.ceil((distance.subtract(BASE_DISTANCE).getDistance()) / FIRST_ADDITIONAL_DISTANCE_INTERVAL.getDistance()) * ADDITIONAL_FARE));
        }
        if (distance.compareTo(ADDITIONAL_FARE_DISTANCE) > 0) {
            additionalFare = (800 + (int) ((Math.ceil((distance.subtract(ADDITIONAL_FARE_DISTANCE).getDistance()
                    / SECOND_ADDITIONAL_DISTANCE_INTERVAL.getDistance())) * ADDITIONAL_FARE)));
        }
        return BASIC_FARE + additionalFare;
    }
}
