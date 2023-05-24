package subway.domain.fare.strategy;

import static java.lang.Math.ceil;
import static subway.domain.fare.strategy.FareDistanceType.FIFTY;
import static subway.domain.fare.strategy.FareDistanceType.FIVE;
import static subway.domain.fare.strategy.FareDistanceType.TEN;
import static subway.domain.fare.strategy.FareType.BASIC_FARE;
import static subway.domain.fare.strategy.FareType.EXTRA_FARE;

import org.springframework.stereotype.Component;
import subway.domain.fare.Fare;
import subway.domain.fare.FareStrategy;
import subway.domain.section.Distance;

@Component
public class DistanceTenFareStrategy implements FareStrategy {

    private static final int MIN_OVER_DISTANCE = 1;

    @Override
    public Fare calculate(final Distance distance) {
        final double overDistance = calculateOverDistance(distance);
        final Fare additionalFare = new Fare(
                (int) ceil(overDistance / FIVE.distance().distance()) * EXTRA_FARE.fare().getFare());
        return BASIC_FARE.fare().addFare(additionalFare);
    }

    private int calculateOverDistance(final Distance distance) {
        if (TEN.distance().sameDistance(distance)) {
            return MIN_OVER_DISTANCE;
        }

        return TEN.distance().subtract(distance).distance();
    }

    //50..10
    @Override
    public boolean isSatisfied(final Distance distance) {
        return (FIFTY.distance().moreAndEqualsThan(distance)) && (TEN.distance().lessAndEqualsThan(distance));
    }
}
