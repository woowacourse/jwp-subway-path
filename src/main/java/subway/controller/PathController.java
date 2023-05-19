package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.controller.dto.response.FindShortestPathResponse;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    // get request 제거
    @GetMapping("/shortest-path")
    public ResponseEntity<FindShortestPathResponse> findShortestPath(
        @RequestParam final String startStationName, @RequestParam final String endStationName) {
        final FindShortestPathResponse response = pathService.findShortestPath(startStationName, endStationName);
        return ResponseEntity.ok().body(response);
    }
}
