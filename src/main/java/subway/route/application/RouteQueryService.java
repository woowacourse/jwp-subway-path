package subway.route.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.value_object.Money;
import subway.line.application.LineQueryService;
import subway.line.domain.Line;
import subway.policy.application.ChargePolicyComposite;
import subway.policy.infrastructure.DiscountCondition;
import subway.line.domain.Station;
import subway.route.application.dto.ShortestRouteRequest;
import subway.route.application.dto.ShortestRouteResponse;

@Service
@Transactional(readOnly = true)
public class RouteQueryService {

  private final ChargePolicyComposite chargePolicyComposite;
  private final LineQueryService lineQueryService;

  public RouteQueryService(
      final ChargePolicyComposite chargePolicyComposite,
      final LineQueryService lineQueryService
  ) {
    this.chargePolicyComposite = chargePolicyComposite;
    this.lineQueryService = lineQueryService;
  }

  public ShortestRouteResponse searchShortestRoute(
      final ShortestRouteRequest shortestRouteRequest
  ) {

    final List<Line> lines = lineQueryService.searchAllLine();

    final Station departure = new Station(shortestRouteRequest.getStartStation());
    final Station arrival = new Station(shortestRouteRequest.getEndStation());

    return new ShortestRouteResponse(
        mapToStationNameFrom(JgraphtRouteFinder.findShortestRoute(lines, departure, arrival)),
        searchCost(lines, departure, arrival, shortestRouteRequest.getAge()).getValue(),
        JgraphtRouteFinder.findShortestRouteDistance(lines, departure, arrival).getValue()
    );
  }

  private List<String> mapToStationNameFrom(final List<Station> stations) {
    return stations.stream()
        .map(Station::getName)
        .collect(Collectors.toList());
  }

  private Money searchCost(
      final List<Line> lines, final Station departure,
      final Station arrival, final Integer age
  ) {
    final Money totalPrice = chargePolicyComposite.calculate(lines, departure, arrival);

    return chargePolicyComposite.discount(new DiscountCondition(age), totalPrice);
  }
}
