package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
            @PathVariable("endStationId") Long endStationId,
            @RequestParam int age) {
        return ResponseEntity.ok(pathService.findShortestPath(startStationId, endStationId, age));
    }
}
