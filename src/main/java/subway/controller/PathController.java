package subway.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @PostMapping
    public ResponseEntity<PathResponse> findPath(@Valid @RequestBody PathRequest request) {
        final PathResponse response = pathService.findPath(request);
        return ResponseEntity.ok(response);
    }
}
