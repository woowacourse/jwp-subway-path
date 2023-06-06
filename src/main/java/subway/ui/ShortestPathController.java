package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.ShortestPathService;
import subway.dto.response.ShortestPathResponse;

@RestController
@RequestMapping("/shortest-path")
public class ShortestPathController {

    private final ShortestPathService shortestPathService;

    public ShortestPathController(final ShortestPathService shortestPathService) {
        this.shortestPathService = shortestPathService;
    }

    @GetMapping
    public ResponseEntity<ShortestPathResponse> findShortestPath(
        @RequestParam(name = "start") final String startName,
        @RequestParam(name = "end") final String endName,
        @RequestParam(name = "age") final int age
    ) {
        final ShortestPathResponse shortestPathResponse = shortestPathService.getPath(startName, endName, age);
        return ResponseEntity.ok(shortestPathResponse);
    }
}
