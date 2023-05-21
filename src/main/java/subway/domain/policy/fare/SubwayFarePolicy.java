package subway.domain.policy.fare;

import subway.domain.Money;
import subway.domain.route.Route;

public interface SubwayFarePolicy {

    Money calculate(final Route route);
}
