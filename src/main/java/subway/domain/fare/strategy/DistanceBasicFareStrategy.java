package subway.domain.fare.strategy;

import static subway.domain.fare.strategy.FareDistanceType.TEN;
import static subway.domain.fare.strategy.FareType.BASIC_FARE;

import org.springframework.stereotype.Component;
import subway.domain.fare.Fare;
import subway.domain.fare.FareStrategy;
import subway.domain.section.Distance;

@Component
public class DistanceBasicFareStrategy implements FareStrategy {

    @Override
    public Fare calculate(final Distance distance) {
        return BASIC_FARE.fare();
    }

    @Override
    public boolean isSatisfied(final Distance distance) {
        return TEN.distance().lessThan(distance);
    }
}
