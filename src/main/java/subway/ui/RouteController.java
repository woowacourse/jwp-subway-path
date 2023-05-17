package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.RouteService;
import subway.dto.RouteResponse;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(final RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<RouteResponse> findShortestRoute(@RequestParam Long sourceStationId,
                                                           @RequestParam Long targetStationId) {
        RouteResponse shortestRoute = routeService.findShortestRoute(sourceStationId, targetStationId);

        return ResponseEntity.ok().body(shortestRoute);
    }
}
