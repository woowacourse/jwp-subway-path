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
    public Fare calculate(final SubwayPath subwayPath) {
        int restDistance = subwayPath.getTotalDistance();
        Fare totalFare = new Fare(DEFAULT_FARE);
        if (restDistance > DISTANCE_BOUNDARY_50) {
            final int overDistance = restDistance - DISTANCE_BOUNDARY_50;
            restDistance = DISTANCE_BOUNDARY_50;
            totalFare = totalFare.add(getOverFare(overDistance, UNIT_DISTANCE_FOR_BOUNDARY_50));
        }
        if (restDistance > DISTANCE_BOUNDARY_10) {
            final int overDistance = restDistance - DISTANCE_BOUNDARY_10;
            totalFare = totalFare.add(getOverFare(overDistance, UNIT_DISTANCE_FOR_BOUNDARY_10));
        }
        return totalFare;
    }

    private Fare getOverFare(final int overDistance, final double unitDistanceForBoundary) {
        return new Fare((int) Math.ceil(overDistance / unitDistanceForBoundary) * UNIT_OVER_FARE);
    }
}
