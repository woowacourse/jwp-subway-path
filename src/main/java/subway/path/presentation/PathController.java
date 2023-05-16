package subway.path.presentation;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.presentation.request.QueryShortestPathRequest;
import subway.path.application.PathService;
import subway.path.application.dto.ShortestRouteResponse;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/shortest")
    ResponseEntity<ShortestRouteResponse> findShortestPath(
            @Valid @ModelAttribute final QueryShortestPathRequest request
    ) {
        final String startStationName = request.getStartStationName();
        final String endStationName = request.getEndStationName();
        final ShortestRouteResponse shortestRoute =
                pathService.findShortestRoute(startStationName, endStationName);
        return ResponseEntity.ok(shortestRoute);
    }
}
