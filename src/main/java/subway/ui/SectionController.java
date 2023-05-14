package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.request.CreateSectionRequest;
import subway.application.response.SectionResponse;
import subway.application.SectionService;

import java.net.URI;

@RequestMapping("/sections")
@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Long> createSection(@RequestBody CreateSectionRequest request) {
        final Long saveSectionId = sectionService.saveSection(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/sections/" + saveSectionId))
                .body(saveSectionId);
    }

    @GetMapping("/{sectionId}")
    ResponseEntity<SectionResponse> findSectionBySectionId(@PathVariable Long sectionId) {
        final SectionResponse sectionResponse = sectionService.findBySectionId(sectionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sectionResponse);
    }
}
