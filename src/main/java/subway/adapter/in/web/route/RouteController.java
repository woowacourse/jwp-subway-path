package subway.adapter.in.web.route;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.adapter.in.web.route.dto.FindShortCutRequest;
import subway.application.dto.RouteResponse;
import subway.application.port.in.route.FindRouteResultUseCase;

@RestController
@RequestMapping("/stations/route")
public class RouteController {

    private final FindRouteResultUseCase findRouteResultUseCase;

    public RouteController(final FindRouteResultUseCase findRouteResultUseCase) {
        this.findRouteResultUseCase = findRouteResultUseCase;
    }

    @GetMapping
    public ResponseEntity<RouteResponse> findResultRouteResult(@RequestParam String fromStation,
                                                               @RequestParam String toStation,
                                                               @RequestParam Integer age) {
        final FindShortCutRequest findShortCutRequest = new FindShortCutRequest(fromStation, toStation, age);
        return ResponseEntity.ok(findRouteResultUseCase.findRouteResult(findShortCutRequest));
    }
}
