package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Route;
import subway.domain.Station;
import subway.domain.SubwayChargePolicy;
import subway.service.dto.ShortestRouteRequest;

import java.util.List;

@Service
public class RouteQueryService {

    private final LineQueryService lineQueryService;
    private final SubwayChargePolicy subwayChargePolicy;

    public RouteQueryService(final LineQueryService lineQueryService, final SubwayChargePolicy subwayChargePolicy) {
        this.lineQueryService = lineQueryService;
        this.subwayChargePolicy = subwayChargePolicy;
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

    public int searchLeastCost(final ShortestRouteRequest shortestRouteRequest) {
        final List<Line> lines = lineQueryService.searchAllLine();

        final Route route = new Route(
                lines,
                new Station(shortestRouteRequest.getStartStation()),
                new Station(shortestRouteRequest.getEndStation())
        );

        return subwayChargePolicy.calculate(route);
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
