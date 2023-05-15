package subway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.service.RouteQueryService;
import subway.service.dto.LeastCostRouteResponse;
import subway.service.dto.ShortestRouteRequest;
import subway.service.dto.ShortestRouteResponse;

@RestController
public class RouteController {

    private final RouteQueryService routeQueryService;

    public RouteController(final RouteQueryService routeQueryService) {
        this.routeQueryService = routeQueryService;
    }

    @GetMapping("/shortest-route")
    public ShortestRouteResponse showShortestRoute(final ShortestRouteRequest shortestRouteRequest) {
        return new ShortestRouteResponse(routeQueryService.searchShortestRoute(shortestRouteRequest));
    }

    @GetMapping("/least-cost")
    public LeastCostRouteResponse showLeastCostRoute(final ShortestRouteRequest shortestRouteRequest) {
        return new LeastCostRouteResponse(
                shortestRouteRequest.getStartStation(),
                shortestRouteRequest.getEndStation(),
                routeQueryService.searchLeastCost(shortestRouteRequest),
                routeQueryService.searchShortestDistance(shortestRouteRequest)
        );
    }
}
