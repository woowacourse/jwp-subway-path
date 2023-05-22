package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.ShortestPathService;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;

import javax.validation.Valid;


@RestController
@RequestMapping("/shortestPath")
public class ShortestPathController {

    private final ShortestPathService shortestPathService;

    public ShortestPathController(ShortestPathService shortestPathService) {
        this.shortestPathService = shortestPathService;
    }

    @GetMapping
    public ResponseEntity<ShortestPathResponse> createStation(@Valid @RequestBody ShortestPathRequest shortestPathRequest) {
        ShortestPathResponse path = shortestPathService.getDijkstraShortestPath(shortestPathRequest);
        return ResponseEntity.ok(path);
    }
}
