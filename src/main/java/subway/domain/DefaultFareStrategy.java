package subway.domain;

import static subway.domain.SubwayFareCalculator.DEFAULT_DISTANCE;
import static subway.domain.SubwayFareCalculator.DEFAULT_FARE;

import org.springframework.stereotype.Component;

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
