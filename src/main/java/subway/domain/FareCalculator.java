package subway.domain;

import subway.domain.vo.Distance;

public interface FareCalculator {

    Fare calculate(Distance distance);
}
