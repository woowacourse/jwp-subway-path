package subway.ui;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.domain.Section;
import subway.ui.dto.section.SectionCreateRequest;
import subway.ui.dto.section.SectionDeleteRequest;
import subway.ui.dto.section.SectionResponse;

@RestController
@RequestMapping("/lines/{lineId}/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping
    public ResponseEntity<List<SectionResponse>> findAllSectionsByLineId(
        @PathVariable Long lineId) {
        return ResponseEntity.ok(sectionService.findSectionsByLineId(lineId));
    }

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(@PathVariable Long lineId,
        @RequestBody @Valid SectionCreateRequest sectionCreateRequest) {
        Section section = sectionService.saveSection(sectionCreateRequest, lineId);
        return ResponseEntity.ok().body(SectionResponse.from(section));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId,
        @RequestBody @Valid SectionDeleteRequest sectionDeleteRequest) {
        sectionService.deleteSection(sectionDeleteRequest, lineId);
        return ResponseEntity.noContent().build();
    }
}
