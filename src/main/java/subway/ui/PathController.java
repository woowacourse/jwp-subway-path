package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.application.PathService;
import subway.dto.PathFindingRequest;
import subway.dto.PathResponse;

@RequestMapping("/paths")
@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getPathInfo(@RequestBody PathFindingRequest pathFindingRequest) {
        PathResponse path = pathService.findPathInfo(pathFindingRequest);
        return ResponseEntity.status(HttpStatus.OK).body(path);
    }
}
