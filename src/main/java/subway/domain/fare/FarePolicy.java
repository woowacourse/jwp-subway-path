package subway.domain.fare;

import subway.domain.Distance;

public interface FarePolicy {

    Fare calculate(final Distance distance);
}
