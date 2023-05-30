package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.RouteResponse;
import subway.service.RouteService;

@RestController
public class RouteController {

    private final RouteService routeService;

    public RouteController(final RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/routes")
    public ResponseEntity<RouteResponse> getShortestPath(@RequestParam final Long startStationId, @RequestParam final Long endStationId, @RequestParam final int age) {
        RouteResponse response = routeService.findShortestRoute(startStationId, endStationId, age);

        return ResponseEntity.ok(response);
    }
}
