package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.ui.dto.request.PathRequest;
import subway.ui.dto.response.PathResponse;

@RestController
public class PathController {

    private final SectionService sectionService;

    public PathController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/path")
    public ResponseEntity<PathResponse> findPath(@RequestBody final PathRequest request) {
        PathResponse pathResponse = sectionService.findPath(request);
        return ResponseEntity.ok(pathResponse);
    }
}
