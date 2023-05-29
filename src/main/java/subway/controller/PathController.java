package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;
import subway.service.PathService;

import javax.validation.Valid;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@Valid @RequestBody PathRequest request) {
        PathResponse pathResponse = pathService.findShortestPath(request);
        return ResponseEntity.ok().body(pathResponse);
    }
}
