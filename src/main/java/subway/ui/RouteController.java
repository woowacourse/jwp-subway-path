package subway.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.RouteService;
import subway.dto.RouteRequest;
import subway.dto.RouteResponse;

@RestController
@RequestMapping("/route")
public class RouteController {
    
    private final RouteService routeService;
    
    public RouteController(final RouteService routeService) {
        this.routeService = routeService;
    }
    
    @GetMapping()
    public ResponseEntity<RouteResponse> findRouteBetween(@RequestBody @Valid final RouteRequest routeRequest) {
        final RouteResponse routeStations = this.routeService.findRouteBetween(routeRequest);
        return ResponseEntity.ok(routeStations);
    }
}
