package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.response.ShortestPathResponse;
import subway.service.PathService;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<ShortestPathResponse> findShortestPath(
            @RequestParam("start") final String startStationName, @RequestParam("end") final String endStationName) {
        return ResponseEntity.ok(pathService.findShortestPath(startStationName, endStationName));
    }
}
