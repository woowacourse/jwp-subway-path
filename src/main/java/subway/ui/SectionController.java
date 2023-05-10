package subway.ui;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.dto.SectionCreateRequest;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionResponse;

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
                                              @RequestBody SectionCreateRequest sectionCreateRequest) {
        sectionService.saveSection(lineId, sectionCreateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId,
                                              @RequestBody SectionDeleteRequest sectionDeleteRequest) {
        sectionService.deleteSection(lineId, sectionDeleteRequest);
        return ResponseEntity.noContent().build();
    }
}
