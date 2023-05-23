package subway.domain.fare;

import subway.domain.section.Distance;

public interface FareCalculator {

    Fare calculate(Distance distance);
}
