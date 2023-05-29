package subway.domain.fare;

import subway.domain.section.Distance;

public interface FarePolicy {

    int calculate(final Distance distance);
}
