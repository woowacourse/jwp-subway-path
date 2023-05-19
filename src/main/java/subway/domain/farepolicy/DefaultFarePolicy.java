package subway.domain.farepolicy;

import org.springframework.stereotype.Component;
import subway.domain.path.SubwayPath;

@Component
public class DefaultFarePolicy implements FarePolicy {

    public static final int DEFAULT_FARE = 1250;
    public static final int DISTANCE_BOUNDARY_10 = 10;
    public static final double UNIT_DISTANCE_FOR_BOUNDARY_10 = 5.0;
    public static final int DISTANCE_BOUNDARY_50 = 50;
    public static final double UNIT_DISTANCE_FOR_BOUNDARY_50 = 8.0;
    public static final int UNIT_OVER_FARE = 100;

    @Override
    public int calculate(final SubwayPath subwayPath) {
        int restDistance = subwayPath.getTotalDistance();
        int totalFare = DEFAULT_FARE;
        if (restDistance > DISTANCE_BOUNDARY_50) {
            final int overDistance = restDistance - DISTANCE_BOUNDARY_50;
            restDistance = DISTANCE_BOUNDARY_50;
            final int overFare = getOverFare(overDistance, UNIT_DISTANCE_FOR_BOUNDARY_50);
            totalFare += overFare;
        }
        if (restDistance > DISTANCE_BOUNDARY_10) {
            final int overDistance = restDistance - DISTANCE_BOUNDARY_10;
            final int overFare = getOverFare(overDistance, UNIT_DISTANCE_FOR_BOUNDARY_10);
            totalFare += overFare;
        }
        return totalFare;
    }

    private int getOverFare(final int overDistance, final double unitDistanceForBoundary) {
        return (int) Math.ceil(overDistance / unitDistanceForBoundary) * UNIT_OVER_FARE;
    }

}
