package subway.application.fare;

import org.springframework.stereotype.Component;
import subway.domain.Distance;

@Component
public class DistanceRateFareCalculator implements FareCalculator {

    private static final int DISTANCE_RATE_OVER_10_UNDER_50 = 5;
    private static final int DISTANCE_RATE_OVER_50 = 8;
    private static final int FARE_RATE_OVER_10_UNDER_50 = 100;
    private static final int FARE_RATE_OVER_50 = 100;
    private static final Distance DEFAULT_DISTANCE = Distance.from(10);
    private static final Distance DISTANCE_50 = Distance.from(50);
    private static final Fare DEFAULT_FARE = Fare.from(1250);

    @Override
    public Fare calculateFare(Distance distance) {
        Fare fare = DEFAULT_FARE;

        if (DEFAULT_DISTANCE.isShorterThan(distance)) {
            fare = fare.plus(calculateOver10Under50(distance));
        }

        if (DISTANCE_50.isShorterThan(distance)) {
            fare = fare.plus(calculateOver50(distance));
        }

        return fare;
    }
    
    private int calculateOver10Under50(Distance distance) {
        if (DISTANCE_50.isShorterThan(distance)) {
            return calculate(DISTANCE_50.minus(DEFAULT_DISTANCE), DISTANCE_RATE_OVER_10_UNDER_50, FARE_RATE_OVER_10_UNDER_50);
        }

        return calculate(distance.minus(DEFAULT_DISTANCE), DISTANCE_RATE_OVER_10_UNDER_50, FARE_RATE_OVER_10_UNDER_50);
    }

    private int calculateOver50(Distance distance) {
        return calculate(distance.minus(DISTANCE_50), DISTANCE_RATE_OVER_50, FARE_RATE_OVER_50);
    }

    private static int calculate(Distance distance, int distanceRate, int fareRate) {
        return (int) ((Math.ceil((distance.getDistance() - 1) / distanceRate) + 1) * fareRate);
    }
}
