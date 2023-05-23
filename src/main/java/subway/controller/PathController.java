package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.PathSearchRequest;
import subway.dto.PathSearchResponse;
import subway.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathSearchResponse> findPath(@RequestParam final Long departureId, @RequestParam final Long arrivalId) {
        return ResponseEntity.ok(pathService.getShortestPath(new PathSearchRequest(departureId, arrivalId)));
    }
}
