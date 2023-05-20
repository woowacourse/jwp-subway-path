package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.DiscountCondition;
import subway.domain.Line;
import subway.domain.Money;
import subway.domain.Route;
import subway.domain.Station;
import subway.domain.SubwayChargePolicy;
import subway.domain.SubwayDiscountPolicy;
import subway.service.dto.ShortestRouteRequest;

import java.util.List;

@Service
public class RouteQueryService {

    private final LineQueryService lineQueryService;
    private final SubwayChargePolicy subwayChargePolicy;
    private final SubwayDiscountPolicy subwayDiscountPolicy;

    public RouteQueryService(
            final LineQueryService lineQueryService,
            final SubwayChargePolicy subwayChargePolicy,
            final SubwayDiscountPolicy subwayDiscountPolicy
    ) {
        this.lineQueryService = lineQueryService;
        this.subwayChargePolicy = subwayChargePolicy;
        this.subwayDiscountPolicy = subwayDiscountPolicy;
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

        final Money totalPrice = subwayChargePolicy.calculate(route);

        return subwayDiscountPolicy.discount(new DiscountCondition(shortestRouteRequest.getAge()), totalPrice)
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
