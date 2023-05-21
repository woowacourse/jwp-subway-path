package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.service.PathService;

import javax.validation.Valid;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @PostMapping("/paths")
    public ResponseEntity<ShortestPathResponse> findShortestPath(@Valid @RequestBody ShortestPathRequest shortestPathRequest) {
        ShortestPathResponse shortestPathResponse = pathService.findShortestPath(shortestPathRequest);
        return ResponseEntity.ok().body(shortestPathResponse);
    }
}
