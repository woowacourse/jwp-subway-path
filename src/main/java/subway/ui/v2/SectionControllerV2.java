package subway.ui.v2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.request.CreateSectionRequest;
import subway.application.response.SectionResponse;
import subway.application.v2.SectionServiceV2;

import java.net.URI;

@RequestMapping("/v2/sections")
@RestController
public class SectionControllerV2 {

    private final SectionServiceV2 sectionService;

    public SectionControllerV2(final SectionServiceV2 sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Long> createSection(@RequestBody CreateSectionRequest request) {
        final Long saveSectionId = sectionService.saveSection(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/v2/sections/" + saveSectionId))
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
