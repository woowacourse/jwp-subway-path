package subway.policy.infrastructure;

import java.util.List;
import java.util.Map;
import subway.policy.domain.SubwayFarePolicy;
import subway.value_object.Money;
import subway.line.domain.Line;
import subway.route.infrastructure.EdgeSection;
import subway.route.application.JgraphtRouteFinder;
import subway.line.domain.Station;

public class LineFarePolicy implements SubwayFarePolicy {

  private static final Map<String, Integer> priceMap
      = Map.of("1호선", 500,
      "2호선", 1000);

  @Override
  public Money calculate(final List<Line> lines, final Station departure, final Station arrival) {
    final List<EdgeSection> shortestRouteSections =
        JgraphtRouteFinder.findShortestRouteSections(lines, departure, arrival);

    return shortestRouteSections.stream()
        .reduce(Money.ZERO, (money, edgeSection) ->
                new Money(priceMap.getOrDefault(edgeSection.getLineName(), 0)),
            (Money::max));
  }
}
