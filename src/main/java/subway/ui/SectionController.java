package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionCreateRequest;
import subway.application.SectionDeleteRequest;
import subway.application.SectionService;
import subway.ui.dto.StationResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sections")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createSection(@RequestBody SectionCreateRequest sectionCreateRequest) {
        final long sectionId = sectionService.createSection(sectionCreateRequest).getId();
        final URI uri = URI.create("/sections/" + sectionId);
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{lineId}/stations")
    public void deleteSection(@PathVariable("lineId") Long lineId, @RequestBody SectionDeleteRequest sectionDeleteRequest) {
        sectionService.deleteSection(lineId, sectionDeleteRequest);
        ResponseEntity.noContent().build();
    }

    @GetMapping("/{lineId}/stations")
    public ResponseEntity<List<StationResponse>> findAllByLine(@PathVariable("lineId") Long lineId) {
        return ResponseEntity.ok(sectionService.findAllByLine(lineId));
    }
}
