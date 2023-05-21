package subway.ui;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.RouteShortestService;
import subway.application.response.QueryShortestRouteResponse;

@RequestMapping("/routes")
@RestController
public class RouteController {

    private final RouteShortestService routeService;

    public RouteController(final RouteShortestService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<QueryShortestRouteResponse> findShortestRoute(
            final @Length(min = 1, max = 10, message = "역명의 길이는 1 ~ 10이어야 합니다.") String startStationName,
            final @Length(min = 1, max = 10, message = "역명의 길이는 1 ~ 10이어야 합니다.") String endStationName
    ) {
        final QueryShortestRouteResponse response = routeService.findByStartAndEnd(startStationName, endStationName);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
