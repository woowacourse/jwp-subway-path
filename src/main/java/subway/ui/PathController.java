package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(
            @RequestParam("source")Long sourceStationId,
            @RequestParam("target")Long targetStationId
    ) {
        final PathResponse pathResponse = pathService.computePath(sourceStationId, targetStationId);
        return ResponseEntity.ok().body(pathResponse);
    }
}
