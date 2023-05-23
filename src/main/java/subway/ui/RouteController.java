package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.RouteService;
import subway.dto.RouteResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/route")
public class RouteController {
    private RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<RouteResponse> getShortestRoute(
            @RequestParam("departure-id") @Valid @NotNull(message = "출발역을 입력해야합니다.") long departureId,
            @RequestParam("arrival-id") @Valid @NotNull(message = "도착역을 입력해야합니다.")  long arrivalId) {
        RouteResponse routeResponse = routeService.findShortcut(departureId, arrivalId);
        return ResponseEntity.ok()
                .body(routeResponse);
    }
}
