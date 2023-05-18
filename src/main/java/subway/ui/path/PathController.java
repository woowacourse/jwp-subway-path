package subway.ui.path;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.service.PathService;
import subway.ui.line.dto.ShortestPathResponse;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/shortest")
    public ResponseEntity<ShortestPathResponse> getShortestPath(@RequestParam Long fromId,
                                                                @RequestParam Long toId) {
        final ShortestPathResponse response = pathService.getShortestPath(fromId, toId);

        return ResponseEntity.ok(response);
    }
}
