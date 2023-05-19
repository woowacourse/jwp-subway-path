package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.RouteService;
import subway.dto.RouteResponse;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/route")
public class RouteController {
    private RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("")
    public ResponseEntity<RouteResponse> getShortestRoute(
            @RequestParam("departure-id") long departureId,
            @RequestParam("arrival-id") long arrivalId){
         RouteResponse routeResponse = routeService.findShortcut(departureId,arrivalId);
         return ResponseEntity.ok()
                 .body(routeResponse);
    }
}
