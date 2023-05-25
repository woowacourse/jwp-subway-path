package subway.domain.policy.fare;

import subway.domain.Money;
import subway.domain.route.RouteFinder;
import subway.domain.station.Station;

public interface SubwayFarePolicy {

  Money calculate(final RouteFinder routeFinder, final Station departure, final Station arrival);
}
