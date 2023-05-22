package subway.domain.fare.strategy;

import static subway.domain.fare.SubwayFareCalculator.ADDITIONAL_FARE;
import static subway.domain.fare.SubwayFareCalculator.DEFAULT_DISTANCE;
import static subway.domain.fare.SubwayFareCalculator.DEFAULT_FARE;
import static subway.domain.fare.SubwayFareCalculator.DISTANCE_THRESHOLD;
import static subway.domain.fare.SubwayFareCalculator.INITIAL_INCREASE_DISTANCE;

import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.fare.Fare;

@Component
public class InitialAdditionalFareStrategy implements FareStrategy {

    @Override
    public boolean isApplicable(final Distance distance) {
        return distance.isGreaterThan(DEFAULT_DISTANCE) && distance.isNotGreaterThan(DISTANCE_THRESHOLD);
    }

    @Override
    public Fare calculate(final Distance distance) {
        return DEFAULT_FARE.add(calculateInitialAdditionalFare(distance));
    }

    private Fare calculateInitialAdditionalFare(final Distance distance) {
        final int count = (distance.subtract(DEFAULT_DISTANCE).getValue() - 1) / INITIAL_INCREASE_DISTANCE + 1;
        return ADDITIONAL_FARE.multiply(count);
    }
}
