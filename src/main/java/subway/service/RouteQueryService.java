package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Route;
import subway.domain.Station;
import subway.domain.SubwayPricePolicy;
import subway.service.dto.ShortestRouteRequest;

import java.util.List;

@Service
public class RouteQueryService {

    private final LineQueryService lineQueryService;
    private final SubwayPricePolicy subwayPricePolicy;

    public RouteQueryService(final LineQueryService lineQueryService, final SubwayPricePolicy subwayPricePolicy) {
        this.lineQueryService = lineQueryService;
        this.subwayPricePolicy = subwayPricePolicy;
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

        return subwayPricePolicy.calculate(route);
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
