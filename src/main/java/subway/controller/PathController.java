package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.controller.dto.request.FindShortestPathRequest;
import subway.controller.dto.response.FindShortestPathResponse;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/shortest-path")
    public ResponseEntity<FindShortestPathResponse> findShortestPath(
        @RequestBody final FindShortestPathRequest request) {
        final FindShortestPathResponse response = pathService.findShortestPath(request);
        return ResponseEntity.ok().body(response);
    }
}
