package subway.adapter.in.web.route;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.port.in.route.FindRouteUseCase;
import subway.application.port.in.route.dto.command.FindRouteCommand;
import subway.application.port.in.route.dto.response.RouteQueryResponse;

@RestController
public class FindRouteController {

    private final FindRouteUseCase findRouteUseCase;

    public FindRouteController(final FindRouteUseCase findRouteUseCase) {
        this.findRouteUseCase = findRouteUseCase;
    }

    @GetMapping("/route")
    public ResponseEntity<RouteQueryResponse> findRoute(
            @RequestParam Long sourceStationId,
            @RequestParam Long targetStationId) {
        RouteQueryResponse response = findRouteUseCase.findRoute(
                new FindRouteCommand(sourceStationId, targetStationId));

        return ResponseEntity.ok(response);
    }
}
