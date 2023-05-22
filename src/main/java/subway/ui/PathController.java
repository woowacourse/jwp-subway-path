package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import subway.application.PathService;

@Controller
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(value = "/path", params = {"startStationId", "endStationId"})
    public ResponseEntity<PathResponse> getPath(@RequestParam final long startStationId, @RequestParam final long endStationId) {
        PathResponse pathResponse = pathService.findPath(startStationId, endStationId);
        return ResponseEntity.ok().body(pathResponse);
    }
}
