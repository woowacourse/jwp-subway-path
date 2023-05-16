package subway.route.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.route.application.RouteService;
import subway.route.application.dto.RouteDto;

@RestController
// TODO: 2023/05/16 validated 붙어야 하는지 확인하기
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/routes")
    public ResponseEntity<RouteDto> findRoute(Long source, Long destination) {
        return ResponseEntity.ok(routeService.findRoute(source, destination));
    }
}
