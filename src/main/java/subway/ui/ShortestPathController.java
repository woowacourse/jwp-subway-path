package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

import javax.validation.Valid;


@RestController
@RequestMapping("/shortestPath")
public class ShortestPathController {

    private final PathService pathService;

    public ShortestPathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> createStation(@Valid @RequestBody PathRequest pathRequest) {
        PathResponse path = pathService.getDijkstraShortestPath(pathRequest);
        return ResponseEntity.ok(path);
    }
}
