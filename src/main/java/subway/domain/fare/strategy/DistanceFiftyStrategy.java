package subway.domain.fare.strategy;

import static subway.domain.fare.strategy.FareDistanceType.EIGHT;
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
public class DistanceFiftyStrategy implements FareStrategy {

    @Override
    public Fare calculate(final Distance distance) {
        final Distance fareSectionDistance = FIFTY.distance().subtract(TEN.distance());
        final Distance extraDistance = distance.subtract(FIFTY.distance());
        final Fare extraFare = calculateByDistance(fareSectionDistance, FIVE.distance())
                .addFare(calculateByDistance(extraDistance, EIGHT.distance()));
        return BASIC_FARE.fare().addFare(extraFare);
    }

    private Fare calculateByDistance(final Distance distance, final Distance unitDistance) {
        final Distance dividedDistance = distance.divideAndCeil(unitDistance);
        return new Fare(dividedDistance.distance()).multiply(EXTRA_FARE.fare());
    }

    @Override
    public boolean isSatisfied(final Distance distance) {
        return distance.moreThan(FIFTY.distance());
    }
}
