package subway.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.request.ReadPathRequest;
import subway.dto.response.PathResponse;
import subway.service.PathService;

@RequestMapping("/paths")
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestBody @Valid final ReadPathRequest pathRequest) {
        final PathResponse pathResponse = pathService.findPath(pathRequest.toDto());
        return ResponseEntity.ok(pathResponse);
    }
}
