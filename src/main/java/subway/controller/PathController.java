package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.controller.dto.response.FindShortestPathResponse;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/shortest")
    public ResponseEntity<FindShortestPathResponse> findShortestPath(
        @RequestParam final String startStationName, @RequestParam final String endStationName,
        @RequestParam final int age) {
        final FindShortestPathResponse response = pathService.findShortestPath(startStationName, endStationName, age);
        return ResponseEntity.ok().body(response);
    }
}
