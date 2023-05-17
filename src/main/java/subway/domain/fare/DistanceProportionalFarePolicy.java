package subway.domain.fare;

import static java.lang.Math.ceil;

import org.springframework.stereotype.Component;
import subway.domain.Distance;

@Component
public class DistanceProportionalFarePolicy implements FarePolicy {

    private static final Distance DISTANCE_10 = new Distance(10);
    private static final Distance DISTANCE_50 = new Distance(50);
    private static final int DEFAULT_FARE = 1250;

    @Override
    public Fare calculate(Distance distance) {
        final Fare totalFare = new Fare(DEFAULT_FARE);
        if (distance.isBigger(DISTANCE_50)) {
            final double overStandardDistance = distance.minusValue(DISTANCE_50).getValue();
            final int additionalFare = (int) ceil(overStandardDistance / 8) * 100;
            totalFare.sum(additionalFare);
            distance = DISTANCE_50;
        }
        if (distance.isBigger(DISTANCE_10)) {
            final double overStandardDistance = distance.minusValue(DISTANCE_10).getValue();
            final int additionalFare = (int) ceil(overStandardDistance / 5) * 100;
            totalFare.sum(additionalFare);
        }
        return totalFare;
    }
}
