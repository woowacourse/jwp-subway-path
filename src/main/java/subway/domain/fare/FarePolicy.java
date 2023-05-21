package subway.domain.fare;

import subway.domain.Route;

public interface FarePolicy {

    Fare calculate(Route route);
}
