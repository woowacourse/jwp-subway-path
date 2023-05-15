package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Route;
import subway.service.dto.ShortestRouteRequest;

import java.util.List;

@Service
public class RouteQueryService {

    private final LineQueryService lineQueryService;

    public RouteQueryService(final LineQueryService lineQueryService) {
        this.lineQueryService = lineQueryService;
    }

    public List<String> searchShortestRoute(final ShortestRouteRequest shortestRouteRequest) {

        final List<Line> lines = lineQueryService.searchSectionsAllLine();

        final Route route = new Route(lines);

        return route.findShortestRoute(
                shortestRouteRequest.getStartStation(),
                shortestRouteRequest.getEndStation()
        );
    }
}
