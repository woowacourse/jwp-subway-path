package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.ShortestPathService;
import subway.dto.ShortestPathResponse;

@RestController
@RequestMapping("/shortest-path")
public class ShortestPathController {

    private final ShortestPathService shortestPathService;

    public ShortestPathController(final ShortestPathService shortestPathService) {
        this.shortestPathService = shortestPathService;
    }

    @GetMapping
    public ResponseEntity<ShortestPathResponse> findShortestPath(
        @RequestParam(name = "start") final String startStationName,
        @RequestParam(name = "end") final String endStationName) {
        final ShortestPathResponse shortestPathResponse = shortestPathService.findShortestPath(startStationName,
            endStationName);
        return ResponseEntity.ok(shortestPathResponse);
    }
}
