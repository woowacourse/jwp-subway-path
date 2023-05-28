package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

@RestController
@RequestMapping("/routes")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/shortest-path")
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam Long fromStationId, @RequestParam Long toStationId) {
        return ResponseEntity.ok(pathService.findShortestPath(new PathRequest(fromStationId, toStationId)));
    }
}
