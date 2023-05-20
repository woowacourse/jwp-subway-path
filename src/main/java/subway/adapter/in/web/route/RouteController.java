package subway.adapter.in.web.route;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.dto.RouteResponse;
import subway.application.port.in.route.FindRouteResultUseCase;
import subway.adapter.in.web.route.dto.FindShortCutRequest;

@RestController
@RequestMapping("/stations/route")
public class RouteController {

    private final FindRouteResultUseCase findRouteResultUseCase;

    public RouteController(final FindRouteResultUseCase findRouteResultUseCase) {
        this.findRouteResultUseCase = findRouteResultUseCase;
    }

    @GetMapping
    public ResponseEntity<RouteResponse> findResultRouteResult(@RequestBody FindShortCutRequest findShortCutRequest) {
        return ResponseEntity.ok(findRouteResultUseCase.findRouteResult(findShortCutRequest));
    }
}
