package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.PathResponse;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/subway/path/startStation/{startStationId}/endStation/{endStationId}")
    public ResponseEntity<PathResponse> findPath(@PathVariable Long startStationId, @PathVariable Long endStationId) {
        PathResponse pathResponse = pathService.findPath(startStationId, endStationId);
        return ResponseEntity.ok().body(pathResponse);
    }

}
