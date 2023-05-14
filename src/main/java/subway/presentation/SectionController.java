package subway.presentation;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionService;
import subway.application.dto.SectionDto;
import subway.presentation.dto.request.SectionRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/subway/lines")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> createSection(@PathVariable Long lineId, @Valid @RequestBody SectionRequest request) {
        SectionDto sectionDto = new SectionDto(
                request.getStartStation(),
                request.getEndStation(),
                request.getDistance()
        );
        sectionService.saveSection(lineId, sectionDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @PathVariable Long stationId) {
        sectionService.deleteSection(lineId, stationId);

        return ResponseEntity.noContent().build();
    }
}
