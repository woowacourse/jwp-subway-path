package subway.domain.fare;

import subway.domain.Sections;

public interface FarePolicy {

    int calculate(Sections sections);
}
