package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Money;
import subway.domain.policy.ChargePolicyComposite;
import subway.domain.policy.discount.DiscountCondition;
import subway.domain.route.JgraphtRouteFinder;
import subway.domain.station.Station;
import subway.service.dto.ShortestRouteRequest;
import subway.service.dto.ShortestRouteResponse;

@Service
@Transactional(readOnly = true)
public class RouteQueryService {

  private final ChargePolicyComposite chargePolicyComposite2;
  private final JgraphtRouteFinder jgraphtRouteFinder;

  public RouteQueryService(
      final ChargePolicyComposite chargePolicyComposite2,
      final JgraphtRouteFinder jgraphtRouteFinder
  ) {
    this.chargePolicyComposite2 = chargePolicyComposite2;
    this.jgraphtRouteFinder = jgraphtRouteFinder;
  }

  public ShortestRouteResponse searchShortestRoute(
      final ShortestRouteRequest shortestRouteRequest
  ) {

    final Station departure = new Station(shortestRouteRequest.getStartStation());
    final Station arrival = new Station(shortestRouteRequest.getEndStation());

    return new ShortestRouteResponse(
        mapToStationNameFrom(jgraphtRouteFinder.findShortestRoute(departure, arrival)),
        searchCost(departure, arrival, shortestRouteRequest.getAge()).getValue(),
        jgraphtRouteFinder.findShortestRouteDistance(departure, arrival).getValue()
    );
  }

  private List<String> mapToStationNameFrom(final List<Station> stations) {
    return stations.stream()
        .map(Station::getName)
        .collect(Collectors.toList());
  }

  private Money searchCost(final Station departure, final Station arrival, final Integer age) {
    final Money totalPrice = chargePolicyComposite2.calculate(jgraphtRouteFinder, departure,
        arrival);

    return chargePolicyComposite2.discount(new DiscountCondition(age), totalPrice);
  }
}
