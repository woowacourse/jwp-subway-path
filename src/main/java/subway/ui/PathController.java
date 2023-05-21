package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.path.PathSelectRequest;
import subway.dto.path.ShortestPathSelectResponse;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/path")
    public ResponseEntity<ShortestPathSelectResponse> findPath(@ModelAttribute PathSelectRequest pathSelectRequest) {
        final Long sourceStationId = pathSelectRequest.getSourceStationId();
        final Long targetStationId = pathSelectRequest.getTargetStationId();
        final ShortestPathSelectResponse response = pathService.findShortestPath(sourceStationId, targetStationId);

        return ResponseEntity.ok()
                .body(response);
    }

}
