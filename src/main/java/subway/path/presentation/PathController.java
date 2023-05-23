package subway.path.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.path.presentation.dto.response.PathResponse;
import subway.path.service.PathService;

@RequestMapping("/paths")
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findByDijkstra(
            @RequestParam Long upStationId,
            @RequestParam Long downStationId
    ) {
        PathResponse response = pathService.findByDijkstra(upStationId, downStationId);
        return ResponseEntity.ok().body(response);
    }

}
