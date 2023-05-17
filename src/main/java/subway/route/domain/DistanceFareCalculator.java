package subway.route.domain;

public class DistanceFareCalculator implements FareCalculator {

    static final int MINIMUM_FARE = 1250;
    static final int INCREMENTAL_FARE = 100;
    static final int MID_DISTANCE_THRESHOLD = 10;
    static final int FAR_DISTANCE_THRESHOLD = 50;
    static final int MID_DISTANCE_INCREMENT_UNIT = 5;
    static final int FAR_DISTANCE_INCREMENT_UNIT = 8;
    static final int MAXIMUM_MID_DISTANCE_INCREMENT_COUNT = 8;

    @Override
    public int calculate(int totalDistance) {
        if (totalDistance <= 0) {
            throw new IllegalArgumentException("디버깅 거리는 양수여야 합니다. totalDistance: " + totalDistance);
        }
        if (totalDistance < MID_DISTANCE_THRESHOLD) {
            return MINIMUM_FARE;
        }

        int midDistanceIncrementCount = getMidDistanceIncrementCount(totalDistance);
        int farDistanceIncrementCount = getFarDistanceIncrementCount(totalDistance);

        return MINIMUM_FARE + INCREMENTAL_FARE * (midDistanceIncrementCount + farDistanceIncrementCount);
    }

    private int getMidDistanceIncrementCount(int totalDistance) {
        if (totalDistance >= FAR_DISTANCE_THRESHOLD) {
            return MAXIMUM_MID_DISTANCE_INCREMENT_COUNT;
        }
        return (totalDistance - MID_DISTANCE_THRESHOLD) / MID_DISTANCE_INCREMENT_UNIT;
    }

    private int getFarDistanceIncrementCount(int totalDistance) {
        if (totalDistance < FAR_DISTANCE_THRESHOLD) {
            return 0;
        }
        return (totalDistance - FAR_DISTANCE_THRESHOLD) / FAR_DISTANCE_INCREMENT_UNIT;
    }
}
