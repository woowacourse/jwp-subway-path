package subway.domain;

import org.springframework.stereotype.Component;

@Component
public class FarePolicy {
    private static final int FARE_INCREASE_UNIT = 100;

    private static final int DEFAULT_FARE = 1_250;
    private static final int DEFAULT_DISTANCE = 10;

    private static final double MID_DISTANCE_UNIT = 5;
    private static final int MID_DISTANCE = 50;
    private static final int MID_DISTANCE_MAXIMUM_EXTRA_FARE = 800;

    private static final int LONG_DISTANCE_UNIT = 8;


    public int calculateFare(int distance) {
        if (distance < DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }
        int midDistanceExtraFare = (int) Math.ceil((double) (distance - DEFAULT_DISTANCE) / MID_DISTANCE_UNIT) * FARE_INCREASE_UNIT;
        if (distance < MID_DISTANCE) {
            return DEFAULT_FARE + midDistanceExtraFare;
        }
        int longDistanceExtraFare = (int) Math.ceil((double) (distance - MID_DISTANCE) / LONG_DISTANCE_UNIT) * FARE_INCREASE_UNIT;
        return DEFAULT_FARE + MID_DISTANCE_MAXIMUM_EXTRA_FARE + longDistanceExtraFare;
    }
}
