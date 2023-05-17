package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Route;
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

        final Route route = new Route(lines);

        return route.findShortestRoute(
                shortestRouteRequest.getStartStation(),
                shortestRouteRequest.getEndStation()
        );
    }

    public int searchLeastCost(final ShortestRouteRequest shortestRouteRequest) {
        return subwayPricePolicy.calculate(searchShortestDistance(shortestRouteRequest));
    }

    public int searchShortestDistance(final ShortestRouteRequest shortestRouteRequest) {
        final List<Line> lines = lineQueryService.searchAllLine();

        final Route route = new Route(lines);

        return route.findShortestRouteDistance(
                shortestRouteRequest.getStartStation(),
                shortestRouteRequest.getEndStation()
        );
    }
}
