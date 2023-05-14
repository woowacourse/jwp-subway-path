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
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.dto.section.SectionResponse;

@RestController
@RequestMapping("/sections")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/{lindId}")
    public ResponseEntity<List<SectionResponse>> findAllSectionsByLineId(@PathVariable Long lindId) {
        return ResponseEntity.ok(sectionService.findSectionsByLineId(lindId));
    }

    @PostMapping("/{lineId}")
    public ResponseEntity<Void> createSection(@PathVariable Long lineId,
                                              @RequestBody @Valid SectionCreateRequest sectionCreateRequest) {
        sectionService.saveSection(lineId, sectionCreateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId,
                                              @RequestBody @Valid SectionDeleteRequest sectionDeleteRequest) {
        sectionService.deleteSection(lineId, sectionDeleteRequest);
        return ResponseEntity.noContent().build();
    }
}
