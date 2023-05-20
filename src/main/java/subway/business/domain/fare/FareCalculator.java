package subway.business.domain.fare;

import java.util.List;

public class FareCalculator {
    private static final int BASIC_FARE = 1250;
    private final List<DistanceFareStrategy> distanceStrategies;

    public FareCalculator(List<DistanceFareStrategy> distanceStrategies) {
        this.distanceStrategies = distanceStrategies;
    }

    public int calculateByDistance(int distance) {
        int totalFare = BASIC_FARE;
        for(DistanceFareStrategy distanceFareStrategy : distanceStrategies) {
            totalFare += distanceFareStrategy.calculateFare(distance);
        }
        return totalFare;
    }
}
