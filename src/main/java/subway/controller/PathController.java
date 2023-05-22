package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {
    public final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/shortestPath")
    public ResponseEntity<PathResponse> findShortestPath(@RequestBody PathRequest pathRequest) {
        return ResponseEntity.ok(pathService.findShortestPath(pathRequest));
    }
}
