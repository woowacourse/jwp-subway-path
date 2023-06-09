package subway.route.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.route.application.RouteQueryService;
import subway.route.dto.request.RouteFindRequest;
import subway.route.dto.response.RouteFindResponse;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteQueryService routeQueryService;

    public RouteController(RouteQueryService routeQueryService) {
        this.routeQueryService = routeQueryService;
    }

    @PostMapping
    public ResponseEntity<RouteFindResponse> findRoute(@RequestBody @Valid RouteFindRequest request) {
        RouteFindResponse responseDto = routeQueryService.findRoute(request);
        return ResponseEntity.ok(responseDto);
    }
}
