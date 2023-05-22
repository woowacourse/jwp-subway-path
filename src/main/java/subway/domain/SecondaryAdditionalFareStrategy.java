package subway.domain;

import static subway.domain.SubwayFareCalculator.ADDITIONAL_FARE;
import static subway.domain.SubwayFareCalculator.COUNT_TO_INITIAL_INCREASE;
import static subway.domain.SubwayFareCalculator.DEFAULT_FARE;
import static subway.domain.SubwayFareCalculator.DISTANCE_THRESHOLD;
import static subway.domain.SubwayFareCalculator.SECONDARY_INCREASE_DISTANCE;

import org.springframework.stereotype.Component;

@Component
public class SecondaryAdditionalFareStrategy implements FareStrategy {

    @Override
    public boolean isApplicable(final Distance distance) {
        return distance.isGreaterThan(DISTANCE_THRESHOLD);
    }

    @Override
    public Fare calculate(final Distance distance) {
        return DEFAULT_FARE.add(calculateSecondaryAdditionalFare(distance));
    }

    private Fare calculateSecondaryAdditionalFare(final Distance distance) {
        final int count = (distance.subtract(DISTANCE_THRESHOLD).getValue() - 1) / SECONDARY_INCREASE_DISTANCE + 1;
        return ADDITIONAL_FARE.multiply(COUNT_TO_INITIAL_INCREASE + count);
    }
}
