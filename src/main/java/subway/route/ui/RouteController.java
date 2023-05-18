package subway.route.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.route.application.RouteService;
import subway.route.application.dto.RouteDto;
import subway.route.application.dto.RouteReadDto;

import javax.validation.Valid;

@RestController
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/routes")
    public ResponseEntity<RouteResponse> findRoute(@Valid RouteReadDto routeReadDto) {
        final RouteDto routeDto = routeService.findRoute(routeReadDto);
        return ResponseEntity.ok(RouteResponse.from(routeDto));
    }
}
