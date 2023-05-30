package subway.domain.fare;

import subway.domain.route.Route;

public interface FarePolicy {

    Fare calculate(Route route, Integer age, Fare fare);
}
