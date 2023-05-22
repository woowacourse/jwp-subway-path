package subway.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.service.PathService;

@RestController
@RequestMapping("/path")
public class PathController {
    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<ShortestPathResponse> findShortestPath(@Valid @RequestBody ShortestPathRequest pathRequest) {
        ShortestPathResponse shortestPathResponse = pathService.findShortestPath(pathRequest);

        return ResponseEntity.ok(shortestPathResponse);
    }
}
