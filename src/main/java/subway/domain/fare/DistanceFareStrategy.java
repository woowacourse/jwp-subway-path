package subway.domain.fare;

import static java.lang.Math.ceil;

import subway.domain.section.Distance;

public class DistanceFareStrategy implements FareStrategy {

    private static final Distance DISTANCE_50 = new Distance(50);
    private static final Distance DISTANCE_10 = new Distance(10);
    private static final Fare DEFAULT_FARE = new Fare(1250);

    @Override
    public Fare calculate(final Distance distance) {

        if (distance.isLongerThan(DISTANCE_10)) {
            final double overDistance = distance.subtract(DISTANCE_10).distance();
            final Fare additionalFare = new Fare((int) ceil(overDistance / 5) * 100);
            return DEFAULT_FARE.addFare(additionalFare);
        }

        if (distance.isLongerThan(DISTANCE_50)) {
            final double overDistance = distance.subtract(DISTANCE_50).distance();
            final Fare additionalFare = new Fare((int) ceil(overDistance / 8) * 100);
            return DEFAULT_FARE.addFare(additionalFare);
        }

        return DEFAULT_FARE;
    }
}
