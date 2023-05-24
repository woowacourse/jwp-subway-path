package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.ShortestPathResponse;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<ShortestPathResponse> findShortestPath(
            @RequestParam("start") Long upStationId,
            @RequestParam("end") Long downStationId
    ) {
        final ShortestPathResponse shortestPathResponse = pathService.findShortestPath(upStationId, downStationId);
        return ResponseEntity.ok(shortestPathResponse);
    }
}
