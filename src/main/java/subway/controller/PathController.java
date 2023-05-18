package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/{departureId}/{arrivalId}")
    public ResponseEntity<PathSearchResponse> findPath(@PathVariable final Long departureId, @PathVariable final Long arrivalId) {
        return ResponseEntity.ok(pathService.getShortestPath(new PathSearchRequest(departureId, arrivalId)));
    }
}
