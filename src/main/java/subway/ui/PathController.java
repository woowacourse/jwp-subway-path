package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> showShortestPath(@RequestParam final Long upStationId,
                                                         @RequestParam final Long downStationId) {
        final PathResponse pathResponse = pathService.findShortestPath(upStationId, downStationId);
        return ResponseEntity.ok().body(pathResponse);
    }
}
