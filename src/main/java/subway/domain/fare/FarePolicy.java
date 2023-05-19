package subway.domain.fare;

import subway.domain.line.Distance;

public interface FarePolicy {

    Fare calculate(Distance distance);
}
