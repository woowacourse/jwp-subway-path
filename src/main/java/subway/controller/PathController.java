package subway.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.service.PathService;
import subway.service.dto.PathRequest;
import subway.service.dto.PathResponse;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@Valid @RequestBody final PathRequest pathRequest) {
        final PathResponse pathResponse = pathService.findPath(pathRequest);
        return ResponseEntity.ok(pathResponse);
    }
}
