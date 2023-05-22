package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.application.dto.path.ShortestPathDto;
import subway.ui.dto.path.ShortestPathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<ShortestPathResponse> findPath(@RequestParam Long sourceStationId,
                                                         @RequestParam Long destStationId) {
        ShortestPathDto shortestPath = pathService.findPath(sourceStationId, destStationId);
        return ResponseEntity.ok(ShortestPathResponse.from(shortestPath));
    }
}
