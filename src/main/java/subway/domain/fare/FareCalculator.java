package subway.domain.fare;

import subway.domain.Distance;

public interface FareCalculator {

    Fare calculate(Distance distance);
}
