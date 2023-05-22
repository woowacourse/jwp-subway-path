package subway.application.fare;

import subway.domain.Fare;

@FunctionalInterface
public interface FareCalculator {

    Fare calculateFare(FareCondition fareCondition);
}
