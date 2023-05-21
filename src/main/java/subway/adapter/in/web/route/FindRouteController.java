package subway.adapter.in.web.route;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.adapter.in.web.route.dto.FindRouteRequest;
import subway.application.port.in.route.FindRouteUseCase;
import subway.application.port.in.route.dto.response.RouteQueryResponse;

@RestController
public class FindRouteController {

    private final FindRouteUseCase findRouteUseCase;

    public FindRouteController(final FindRouteUseCase findRouteUseCase) {
        this.findRouteUseCase = findRouteUseCase;
    }

    @GetMapping("/route")
    public ResponseEntity<RouteQueryResponse> findRoute(
            @RequestBody @Valid FindRouteRequest request) {
        RouteQueryResponse response = findRouteUseCase.findRoute(request.toCommand());

        return ResponseEntity.ok(response);
    }
}
