package subway.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public ResponseEntity<PathResponse> findPath(@RequestBody @Valid final ReadPathRequest pathRequest) {
        final PathResponse pathResponse = pathService.findPath(pathRequest.toDto());
        return ResponseEntity.ok(pathResponse);
    }
}
