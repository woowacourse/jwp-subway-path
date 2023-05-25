package subway.domain.policy.fare;

import subway.domain.Money;
import subway.domain.route.RouteFinder;

public interface SubwayFarePolicy {

  Money calculate(final RouteFinder routeFinder, final String departure, final String arrival);
}
