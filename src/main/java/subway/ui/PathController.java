package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.PathResponse;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getShortestPath(@RequestParam("departure") final Long departureId,
                                                        @RequestParam("arrival") final Long arrivalId) {
        final PathResponse pathResponse = pathService.findShortestPath(departureId, arrivalId);
        return ResponseEntity.ok(pathResponse);
    }
}
