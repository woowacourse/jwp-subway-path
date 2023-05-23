package subway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import subway.service.RouteQueryService;
import subway.service.dto.ShortestRouteRequest;
import subway.service.dto.ShortestRouteResponse;

@RestController
public class RouteController {

  private final RouteQueryService routeQueryService;

  public RouteController(final RouteQueryService routeQueryService) {
    this.routeQueryService = routeQueryService;
  }

  @GetMapping("/route")
  public ShortestRouteResponse showShortestRoute(
      @ModelAttribute ShortestRouteRequest shortestRouteRequest) {
    return new ShortestRouteResponse(
        routeQueryService.searchShortestRoute(shortestRouteRequest),
        routeQueryService.searchLeastCost(shortestRouteRequest),
        routeQueryService.searchShortestDistance(shortestRouteRequest)
    );
  }
}
