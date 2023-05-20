package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import subway.application.PathService;
import subway.dto.PathResponse;

@Controller
@RequestMapping("/paths")
public final class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping()
    public ResponseEntity<PathResponse> findPath(@RequestParam Long startStationId, @RequestParam Long endStationId) {
        PathResponse pathResponse = pathService.findPathWithCost(startStationId, endStationId);
        return ResponseEntity.ok(pathResponse);
    }
}
