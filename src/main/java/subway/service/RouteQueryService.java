package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Money;
import subway.domain.policy.ChargePolicyComposite;
import subway.domain.policy.discount.DiscountCondition;
import subway.domain.route.RouteFinder;
import subway.service.dto.ShortestRouteRequest;
import subway.service.dto.ShortestRouteResponse;

@Service
@Transactional(readOnly = true)
public class RouteQueryService {

  private final ChargePolicyComposite chargePolicyComposite2;
  private final RouteFinder routeFinder;

  public RouteQueryService(
      final ChargePolicyComposite chargePolicyComposite2,
      final RouteFinder routeFinder
  ) {
    this.chargePolicyComposite2 = chargePolicyComposite2;
    this.routeFinder = routeFinder;
  }

  public ShortestRouteResponse searchShortestRoute(
      final ShortestRouteRequest shortestRouteRequest
  ) {

    final String departure = shortestRouteRequest.getStartStation();
    final String arrival = shortestRouteRequest.getEndStation();

    return new ShortestRouteResponse(
        routeFinder.findShortestRoute(departure, arrival),
        searchCost(departure, arrival, shortestRouteRequest.getAge()).getValue(),
        routeFinder.findShortestRouteDistance(departure, arrival).getValue()
    );
  }

  private Money searchCost(final String departure, final String arrival, final Integer age) {
    final Money totalPrice = chargePolicyComposite2.calculate(routeFinder, departure, arrival);

    return chargePolicyComposite2.discount(new DiscountCondition(age), totalPrice);
  }
}
