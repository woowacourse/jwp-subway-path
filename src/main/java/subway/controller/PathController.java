package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.response.PathResponse;
import subway.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(
            @RequestParam("from") long departureStationId,
            @RequestParam("to") long arrivalStationId,
            @RequestParam("age") int age
    ) {
        PathResponse path = pathService.findPath(departureStationId, arrivalStationId, age);
        return ResponseEntity.ok().body(path);
    }
}
