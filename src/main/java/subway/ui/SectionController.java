package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionCreateRequest;
import subway.application.SectionDeleteRequest;
import subway.application.SectionService;
import subway.ui.dto.StationResponse;

import java.util.List;

@RestController
@RequestMapping("/lines")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/{line_id}/stations")
    public void createSection(@PathVariable("line_id") Long line_id, @RequestBody SectionCreateRequest sectionCreateRequest) {
        sectionService.createSection(line_id, sectionCreateRequest);
        ResponseEntity.ok().build();
    }

    @DeleteMapping("/{line_id}/stations")
    public void deleteSection(@PathVariable("line_id") Long line_id, @RequestBody SectionDeleteRequest sectionDeleteRequest) {
        sectionService.deleteSection(line_id, sectionDeleteRequest);
        ResponseEntity.noContent().build();
    }

    @GetMapping("/{line_id}/stations")
    public ResponseEntity<List<StationResponse>> findAllByLine(@PathVariable("line_id") Long line_id) {
        return ResponseEntity.ok(sectionService.findAllByLine(line_id));
    }
}
