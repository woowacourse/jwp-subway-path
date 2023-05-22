package subway.domain.fare.strategy;

import static subway.domain.fare.SubwayFareCalculator.DEFAULT_DISTANCE;
import static subway.domain.fare.SubwayFareCalculator.DEFAULT_FARE;

import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.fare.Fare;

@Component
public class DefaultFareStrategy implements FareStrategy {

    @Override
    public boolean isApplicable(final Distance distance) {
        return distance.isNotGreaterThan(DEFAULT_DISTANCE);
    }

    @Override
    public Fare calculate(final Distance distance) {
        return DEFAULT_FARE;
    }
}
