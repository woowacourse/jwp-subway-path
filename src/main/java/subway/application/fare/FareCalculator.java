package subway.application.fare;

import subway.domain.Distance;
import subway.domain.Fare;

public interface FareCalculator {

    Fare calculateFare(Distance distance);
}
