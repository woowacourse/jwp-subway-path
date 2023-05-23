package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Money;
import subway.domain.line.Line;
import subway.domain.policy.ChargePolicyComposite;
import subway.domain.policy.discount.DiscountCondition;
import subway.domain.route.Route;
import subway.domain.station.Station;
import subway.service.dto.ShortestRouteRequest;

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

  public List<String> searchShortestRoute(final ShortestRouteRequest shortestRouteRequest) {

    final List<Line> lines = lineQueryService.searchAllLine();

    final Route route = new Route(
        lines,
        new Station(shortestRouteRequest.getStartStation()),
        new Station(shortestRouteRequest.getEndStation())
    );

    return route.findShortestRoute();
  }

  public double searchLeastCost(final ShortestRouteRequest shortestRouteRequest) {
    final List<Line> lines = lineQueryService.searchAllLine();

    final Route route = new Route(
        lines,
        new Station(shortestRouteRequest.getStartStation()),
        new Station(shortestRouteRequest.getEndStation())
    );

    final Money totalPrice = chargePolicyComposite.calculate(route);

    return chargePolicyComposite.discount(new DiscountCondition(shortestRouteRequest.getAge()),
            totalPrice)
        .getValue();
  }

  public int searchShortestDistance(final ShortestRouteRequest shortestRouteRequest) {
    final List<Line> lines = lineQueryService.searchAllLine();

    final Route route = new Route(
        lines,
        new Station(shortestRouteRequest.getStartStation()),
        new Station(shortestRouteRequest.getEndStation())
    );

    return route.findShortestRouteDistance()
        .getValue();
  }
}
