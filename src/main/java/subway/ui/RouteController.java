package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.RouteService;
import subway.application.dto.RouteResponse;

@RequestMapping("/routes")
@RestController
public class RouteController {

    private final RouteService routeService;

    public RouteController(final RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<RouteResponse> getShortestRoute(@RequestParam("sourceStationId") final Long sourceStationId,
                                                          @RequestParam("targetStationId") final Long targetStationId) {
        final RouteResponse routeResponse = routeService.getShortestRouteAndFare(sourceStationId, targetStationId);
        return ResponseEntity.ok(routeResponse);
    }
}
