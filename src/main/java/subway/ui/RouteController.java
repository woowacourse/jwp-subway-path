package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.RouteService;
import subway.application.response.QueryShortestRouteResponse;

@RequestMapping("/routes")
@RestController
public class RouteController {

    private final RouteService routeService;

    public RouteController(final RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<QueryShortestRouteResponse> findShortestRoute(
            @RequestParam(name = "passengerAge") int passengerAge,
            @RequestParam(name = "startStationName")  String startStationName,
            @RequestParam(name = "endStationName") String endStationName
    ) {
        final QueryShortestRouteResponse response
                = routeService.findByStartAndEnd(passengerAge, startStationName, endStationName);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
