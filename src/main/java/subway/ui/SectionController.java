package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionService;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

import java.net.URI;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(@RequestBody final SectionRequest sectionRequest) {
        Long sectionId = sectionService.saveSection(sectionRequest);
        return ResponseEntity.created(URI.create("/sections/" + sectionId)).body(new SectionResponse(sectionId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@RequestParam final Long lineId, @RequestParam final Long stationId) {
        sectionService.deleteSectionByStationId(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
