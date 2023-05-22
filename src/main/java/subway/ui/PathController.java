package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.service.PathService;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/shortestPath/{id}")
    public ResponseEntity<ShortestPathResponse> showShortestPath(@PathVariable Long id, @RequestBody ShortestPathRequest request) {
        return ResponseEntity.ok(pathService.findShortestPath(id, request));
    }
}
