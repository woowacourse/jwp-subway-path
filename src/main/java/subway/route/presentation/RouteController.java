package subway.route.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import subway.route.application.RouteQueryService;
import subway.route.application.dto.ShortestRouteRequest;
import subway.route.application.dto.ShortestRouteResponse;

@RestController
public class RouteController {

  private final RouteQueryService routeQueryService;

  public RouteController(final RouteQueryService routeQueryService) {
    this.routeQueryService = routeQueryService;
  }

  @GetMapping("/route")
  public ShortestRouteResponse showShortestRoute(
      @ModelAttribute ShortestRouteRequest shortestRouteRequest
  ) {
    return routeQueryService.searchShortestRoute(shortestRouteRequest);
  }
}
