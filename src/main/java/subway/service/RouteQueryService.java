package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Distance;
import subway.domain.Money;
import subway.domain.line.Line;
import subway.domain.policy.ChargePolicyComposite;
import subway.domain.policy.discount.DiscountCondition;
import subway.domain.route.Route;
import subway.domain.station.Station;
import subway.service.dto.ShortestRouteRequest;
import subway.service.dto.ShortestRouteResponse;

@Service
@Transactional(readOnly = true)
public class RouteQueryService {

  private final LineQueryService lineQueryService;
  private final ChargePolicyComposite chargePolicyComposite;

  public RouteQueryService(
      final LineQueryService lineQueryService,
      final ChargePolicyComposite chargePolicyComposite
  ) {
    this.lineQueryService = lineQueryService;
    this.chargePolicyComposite = chargePolicyComposite;
  }

  public ShortestRouteResponse searchShortestRoute(
      final ShortestRouteRequest shortestRouteRequest
  ) {

    final List<Line> lines = lineQueryService.searchAllLine();

    final Route route = new Route(
        lines,
        new Station(shortestRouteRequest.getStartStation()),
        new Station(shortestRouteRequest.getEndStation())
    );

    return new ShortestRouteResponse(
        route.findShortestRoute(),
        searchCost(route, shortestRouteRequest.getAge()).getValue(),
        searchShortestDistance(route).getValue()
    );
  }

  private Money searchCost(final Route route, final Integer age) {
    final Money totalPrice = chargePolicyComposite.calculate(route);

    return chargePolicyComposite.discount(new DiscountCondition(age), totalPrice);
  }

  private Distance searchShortestDistance(final Route route) {
    return route.findShortestRouteDistance();
  }
}
