package subway.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.RouteService;
import subway.dto.RouteRequest;
import subway.dto.RouteResponse;

@RestController
@RequestMapping("subway/route")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<RouteResponse> findRoute(@RequestBody @Valid RouteRequest routeRequest) {
        RouteResponse routeResponse = RouteResponse.of(
                routeService.findShortestRoute(routeRequest),
                routeService.findShortestDistance(routeRequest)
        );
        return ResponseEntity.ok(routeResponse);
    }

}
