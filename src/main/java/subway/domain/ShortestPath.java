package subway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ShortestPath {
    private static final int BASIC_FARE = 1250;
    public static final double BASE_DISTANCE = 10;
    private static final double ADDITIONAL_FARE_DISTANCE = 50;
    public static final int FIRST_ADDITIONAL_DISTANCE_INTERVAL = 5;
    public static final int SECOND_ADDITIONAL_DISTANCE_INTERVAL = 8;
    public static final int ADDITIONAL_FARE = 100;

    private final List<Station> stations;
    private final double distance;
    private final int fare;

    public static ShortestPath of(List<Station> stations, double distance) {
        int fare = calculateFare(distance);
        return new ShortestPath(stations, distance, fare);
    }

    private static int calculateFare(double distance) {
        int additionalFare = 0;
        if (distance > BASE_DISTANCE && distance <= ADDITIONAL_FARE_DISTANCE) {
            additionalFare = ((int)((distance - BASE_DISTANCE) / FIRST_ADDITIONAL_DISTANCE_INTERVAL) * ADDITIONAL_FARE);
        }
        if (distance > ADDITIONAL_FARE_DISTANCE) {
            additionalFare = (800 + ((int)(distance - ADDITIONAL_FARE_DISTANCE) / SECOND_ADDITIONAL_DISTANCE_INTERVAL) * ADDITIONAL_FARE);
        }
        return BASIC_FARE + additionalFare;
    }
}
