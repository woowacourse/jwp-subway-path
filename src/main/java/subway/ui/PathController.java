package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.business.service.PathService;
import subway.business.service.dto.ShortestPathResponse;

@RestController
@RequestMapping("/path")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<ShortestPathResponse> getShortestPath(@RequestParam Long sourceStationId, @RequestParam Long destStationId) {
        return ResponseEntity.ok(pathService.getShortestPath(sourceStationId, destStationId));
    }
}
