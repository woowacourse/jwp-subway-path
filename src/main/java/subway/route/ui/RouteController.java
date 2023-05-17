package subway.route.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.route.application.RouteService;
import subway.route.application.dto.RouteDto;

@RestController
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/routes")
    public ResponseEntity<RouteDto> findRoute(@RequestParam("source") Long source, @RequestParam("destination") Long destination) {
        return ResponseEntity.ok(routeService.findRoute(source, destination));
    }
}
