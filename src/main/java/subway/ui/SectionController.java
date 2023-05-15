package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionCreateRequest;
import subway.application.SectionDeleteRequest;
import subway.application.SectionService;
import subway.ui.dto.StationResponse;

import java.util.List;

@RestController
@RequestMapping("/line")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/{lineId}/stations")
    public void createSection(@PathVariable("lineId") Long lineId, @RequestBody SectionCreateRequest sectionCreateRequest) {
        sectionService.createSection(lineId, sectionCreateRequest);
        ResponseEntity.ok().build();
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
