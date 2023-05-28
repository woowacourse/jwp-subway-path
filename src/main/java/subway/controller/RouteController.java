package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.service.RouteService;
import subway.service.dto.response.RouteFindingResponse;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<RouteFindingResponse> findShortestPath(@RequestParam String startStation, @RequestParam String endStation) {
        return ResponseEntity.ok(routeService.findShortestPath(startStation, endStation));
    }

}
