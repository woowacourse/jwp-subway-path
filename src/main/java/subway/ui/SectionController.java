package subway.ui;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionService;
import subway.dto.SectionRequest;

import java.net.URI;
import subway.dto.SectionResponse;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> createSection(@RequestBody final SectionRequest sectionRequest) {
        sectionService.save(sectionRequest);
        return ResponseEntity.created(URI.create("/sections")).build();
    }

    @GetMapping
    public ResponseEntity<List<SectionResponse>> showSections(@RequestParam final Long lineId) {
        return ResponseEntity.ok().body(sectionService.findByLineId(lineId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@RequestParam final Long lineId, @RequestParam final Long stationId) {
        sectionService.delete(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
