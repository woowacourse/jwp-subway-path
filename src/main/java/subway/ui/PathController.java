package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.application.dto.PathsResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/{startStationId}/{endStationId}")
    public ResponseEntity<PathsResponse> findShortestPaths(@PathVariable final Long startStationId, @PathVariable final Long endStationId) {
        PathsResponse shortestPaths = pathService.findShortestPaths(startStationId, endStationId);
        return ResponseEntity.status(HttpStatus.OK).body(shortestPaths);
    }
}
