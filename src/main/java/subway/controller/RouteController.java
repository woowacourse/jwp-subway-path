package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.service.RouteService;
import subway.service.dto.request.RouteFindingRequest;
import subway.service.dto.response.RouteFindingResponse;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<RouteFindingResponse> findShortestPath(@RequestBody RouteFindingRequest routeFindingRequest) {
        return ResponseEntity.ok(routeService.findShortestPath(routeFindingRequest));
    }

}
