package subway.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.request.ShortestPathRequest;
import subway.dto.response.ShortestPathResponse;
import subway.service.PathService;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/path-shorted")
    public ResponseEntity<ShortestPathResponse> findShortestPath(@RequestBody @Valid ShortestPathRequest shortestPathRequest) {
        ShortestPathResponse response = pathService.findShortestPath(shortestPathRequest);
        return ResponseEntity.ok(response);
    }
}
