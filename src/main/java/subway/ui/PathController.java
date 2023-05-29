package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.PathResponse;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/{startStationId}/{endStationId}")
    public ResponseEntity<PathResponse> shortestPath(
            @PathVariable("startStationId") Long startStationId,
            @PathVariable("endStationId") Long endStationId) {
        return ResponseEntity.ok(pathService.findShortestPath(startStationId, endStationId));
    }
}
