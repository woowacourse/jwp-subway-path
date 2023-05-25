package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Money;
import subway.domain.line.Line;
import subway.domain.policy.ChargePolicyComposite;
import subway.domain.policy.discount.DiscountCondition;
import subway.domain.route.JgraphtRouteFinder;
import subway.domain.station.Station;
import subway.service.dto.ShortestRouteRequest;
import subway.service.dto.ShortestRouteResponse;

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
