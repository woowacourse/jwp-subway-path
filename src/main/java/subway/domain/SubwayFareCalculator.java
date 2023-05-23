package subway.domain;

public class SubwayFareCalculator implements FareCalculator {
    private static final int BASIC_FARE = 1250;
    private static final int DISTANCE_BOUNDARY_10 = 10;
    private static final int DISTANCE_BOUNDARY_50 = 50;
    private static final double UNIT_DISTANCE_FOR_BOUNDARY_10 = 5.0;
    private static final double UNIT_DISTANCE_FOR_BOUNDARY_50 = 8.0;
    private static final int UNIT_OVER_FARE = 100;

    @Override
    public int calculate(int distance) {
        int fare = BASIC_FARE;
        if (distance > DISTANCE_BOUNDARY_50) {
            final int overDistance = distance - DISTANCE_BOUNDARY_50;
            distance = DISTANCE_BOUNDARY_50;
            final int overFare = getOverFare(overDistance, UNIT_DISTANCE_FOR_BOUNDARY_50);
            fare += overFare;
        }
        if (distance > DISTANCE_BOUNDARY_10) {
            final int overDistance = distance - DISTANCE_BOUNDARY_10;
            final int overFare = getOverFare(overDistance, UNIT_DISTANCE_FOR_BOUNDARY_10);
            fare += overFare;
        }
        return fare;
    }

    private int getOverFare(final int overDistance, final double unitDistanceForBoundary) {
        return (int) Math.ceil(overDistance / unitDistanceForBoundary) * UNIT_OVER_FARE;
    }
}
