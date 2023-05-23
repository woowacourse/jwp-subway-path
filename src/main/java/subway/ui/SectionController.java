package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import subway.application.PathService;
import subway.application.request.PathRequest;
import subway.application.response.PathResponse;

import javax.validation.Valid;

@Controller
@RequestMapping("/lines/sections")
public class SectionController {

    private final PathService pathService;

    public SectionController(final PathService pathService) {
        this.pathService = pathService;
    }

    @PostMapping("/path")
    public ResponseEntity<PathResponse> findPath(@Valid @RequestBody final PathRequest pathRequest) {
        final PathResponse response = pathService.findPath(pathRequest);
        return ResponseEntity.ok(response);
    }
}
