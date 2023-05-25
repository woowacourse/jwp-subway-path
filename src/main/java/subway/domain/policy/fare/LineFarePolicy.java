package subway.domain.policy.fare;

import java.util.List;
import java.util.Map;
import subway.domain.Money;
import subway.domain.route.EdgeSection;
import subway.domain.route.RouteFinder;

public class LineFarePolicy implements SubwayFarePolicy {

  private static final Map<String, Integer> priceMap
      = Map.of("1호선", 500,
      "2호선", 1000);

  @Override
  public Money calculate(
      final RouteFinder routeFinder,
      final String departure,
      final String arrival
  ) {
    final List<EdgeSection> shortestRouteSections = routeFinder.findShortestRouteSections(departure,
        arrival);

    return shortestRouteSections.stream()
        .reduce(Money.ZERO, (money, edgeSection) ->
                new Money(priceMap.getOrDefault(edgeSection.getLineName(), 0)),
            (Money::max));
  }
}
