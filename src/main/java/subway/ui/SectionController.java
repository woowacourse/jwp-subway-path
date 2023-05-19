package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import subway.application.SectionService;
import subway.ui.request.PathRequest;
import subway.ui.response.PathResponse;

import javax.validation.Valid;

@Controller
@RequestMapping("/lines/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/path")
    public ResponseEntity<PathResponse> findPath(@Valid @RequestBody final PathRequest pathRequest) {
        final PathResponse response = sectionService.findPath(pathRequest);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
