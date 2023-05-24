package subway.domain.fare.strategy;

import static java.lang.Math.ceil;
import static subway.domain.fare.strategy.FareDistanceType.EIGHT;
import static subway.domain.fare.strategy.FareDistanceType.FIFTY;
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
        final double overDistance = distance.subtract(FIFTY.distance()).distance();
        final Fare additionalFare = new Fare(
                (int) ceil(overDistance / EIGHT.distance().distance()) * EXTRA_FARE.fare().getFare());
        return BASIC_FARE.fare().addFare(additionalFare);
    }

    @Override
    public boolean isSatisfied(final Distance distance) {
        return FIFTY.distance().lessThan(distance);
    }
}
