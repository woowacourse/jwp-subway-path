package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.RoutingService;
import subway.dto.RoutesResponse;

@RequestMapping("/routes")
@RestController
public class RoutingController {

    private final RoutingService routingService;

    public RoutingController(final RoutingService routingService) {
        this.routingService = routingService;
    }

    @GetMapping
    public ResponseEntity<RoutesResponse> findRoutes(@RequestParam Long startStationId,
                                                     @RequestParam Long endStationId) {
        return ResponseEntity.ok(routingService.findRoutes(startStationId, endStationId));
    }
}
