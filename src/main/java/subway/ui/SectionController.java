package subway.ui;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionService;
import subway.dto.SectionRequest;

@RestController
@RequestMapping("/subway/lines")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> createSection(@PathVariable Long lineId,
                                              @RequestBody SectionRequest sectionRequest) {
        sectionService.saveSection(lineId, sectionRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @PathVariable Long stationId) {
        sectionService.deleteSection(lineId, stationId);

        return ResponseEntity.noContent().build();
    }
}

