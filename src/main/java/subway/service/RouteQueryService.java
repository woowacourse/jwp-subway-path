package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.ChargePolicyComposite;
import subway.domain.DiscountCondition;
import subway.domain.Line;
import subway.domain.Money;
import subway.domain.Route;
import subway.domain.Station;
import subway.service.dto.ShortestRouteRequest;

import java.util.List;

@Service
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

        return chargePolicyComposite.discount(new DiscountCondition(shortestRouteRequest.getAge()), totalPrice)
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
