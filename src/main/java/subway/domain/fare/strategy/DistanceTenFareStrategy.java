package subway.domain.fare.strategy;

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

    private static final Distance MIN_EXTRA_DISTANCE = new Distance(1);

    @Override
    public Fare calculate(final Distance distance) {
        final Distance extraDistance = calculateExtraDistance(distance);
        return BASIC_FARE.fare().addFare(calculateByDistance(extraDistance));
    }

    private Distance calculateExtraDistance(final Distance distance) {
        if (distance.isSame(TEN.distance())) {
            return MIN_EXTRA_DISTANCE;
        }

        return distance.subtract(TEN.distance());
    }

    private Fare calculateByDistance(final Distance distance) {
        final Distance dividedDistance = distance.divideAndCeil(FIVE.distance());
        return new Fare(dividedDistance.distance()).multiply(EXTRA_FARE.fare());
    }

    @Override
    public boolean isSatisfied(final Distance distance) {
        return (distance.lessAndEqualsThan(FIFTY.distance())) && (distance.moreAndEqualsThan(TEN.distance()));
    }
}
