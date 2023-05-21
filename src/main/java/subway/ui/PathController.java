package subway.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.application.dto.path.ShortestPathDto;
import subway.ui.dto.path.PathFindRequest;
import subway.ui.dto.path.ShortestPathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<ShortestPathResponse> findPath(@RequestBody @Valid PathFindRequest request) {
        ShortestPathDto shortestPath = pathService.findPath(request.toPathFindDto());
        return ResponseEntity.ok(ShortestPathResponse.from(shortestPath));
    }
}
