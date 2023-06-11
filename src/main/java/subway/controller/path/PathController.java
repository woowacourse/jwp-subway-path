package subway.controller.path;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.shortestpath.ShortestPathRequest;
import subway.dto.shortestpath.ShortestPathResponse;
import subway.service.path.PathService;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/shortest-path")
    ResponseEntity<ShortestPathResponse> findShortestPath(@RequestBody @Valid ShortestPathRequest request) {
        final ShortestPathResponse shortestPath = pathService.findShortestPath(request);
        return ResponseEntity.ok(shortestPath);
    }
}
