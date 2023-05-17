package subway.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.service.PathService;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @PostMapping("/shortest-path")
    public ResponseEntity<ShortestPathResponse> shortestPath(@RequestBody @Valid final ShortestPathRequest request) {
        final ShortestPathResponse response = pathService.shortestPath(request);
        return ResponseEntity.ok(response);
    }
}
