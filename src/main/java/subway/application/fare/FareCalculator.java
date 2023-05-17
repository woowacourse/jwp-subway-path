package subway.application.fare;

import subway.domain.Distance;

public interface FareCalculator {

    Fare calculateFare(Distance distance);
}
