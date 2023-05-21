package subway.controller.path;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.section.dto.PathRequest;
import subway.service.path.PathService;
import subway.service.section.dto.PathResult;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResult> requestPath(@RequestBody PathRequest pathRequest) {
        PathResult pathResult = pathService.calculateShortestPathFee(pathRequest.getSourceStationId(), pathRequest.getTargetStationId());
        return ResponseEntity.ok(pathResult);
    }
}
