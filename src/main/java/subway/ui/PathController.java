package subway.ui;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import subway.application.PathService;
import subway.ui.dto.PathRequest;
import subway.ui.dto.PathResponse;

@Controller
public class PathController {

    PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @PostMapping("/path")
    public ResponseEntity<PathResponse> findPath(@RequestBody @Valid PathRequest pathRequest) {
        return ResponseEntity.ok().body(pathService.findPath(pathRequest));
    }
}
