package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<ShortestPathResponse> findShortestPath(@ModelAttribute @Valid final ShortestPathRequest shortestPathRequest) {
        ShortestPathResponse shortestPath = pathService.findShortestPath(shortestPathRequest);
        return ResponseEntity.ok(shortestPath);
    }
}
