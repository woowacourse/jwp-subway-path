package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.PathResponse;
import subway.service.path.PathService;

@RestController
@RequestMapping("/path")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getShortestPath(@RequestParam("departure") Long departureStationId,
                                                        @RequestParam("arrival") Long arrivalStationId) {
        PathResponse pathResponse = pathService.getShortestPath(departureStationId, arrivalStationId);
        return ResponseEntity.ok().body(pathResponse);
    }
}
