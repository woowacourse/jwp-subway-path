package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import subway.application.SectionService;
import subway.ui.request.PathRequest;
import subway.ui.response.StationResponse;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/lines/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/path")
    public ResponseEntity<List<StationResponse>> findPath(@Valid @RequestBody final PathRequest pathRequest) {
        final List<StationResponse> responses = sectionService.findPath(pathRequest);
        return ResponseEntity.ok(responses);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
