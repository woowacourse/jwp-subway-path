package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.RouteShortestService;
import subway.application.response.QueryShortestRouteResponse;

@RequestMapping("/route")
@RestController
public class RouteController {

    private final RouteShortestService routeService;

    public RouteController(final RouteShortestService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<QueryShortestRouteResponse> findShortestRoute(
            final String startStationName,
            final String endStationName
    ) {
        final QueryShortestRouteResponse response = routeService.findByStartAndEnd(startStationName, endStationName);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
