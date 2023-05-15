package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import subway.ui.dto.response.LineResponse;
import subway.ui.dto.request.SectionRequest;
import subway.application.SectionService;
import subway.ui.dto.response.StationResponse;

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
    public ResponseEntity<StationResponse> createSection(@RequestBody SectionRequest sectionRequest) {
        final long sectionId = sectionService.createSection(sectionRequest).getId();
        final URI uri = URI.create("/sections/" + sectionId);
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{lineId}/stations")
    public void deleteSection(@PathVariable("lineId") Long lineId, @RequestBody LineResponse.SectionDeleteRequest sectionDeleteRequest) {
        sectionService.deleteSection(lineId, sectionDeleteRequest);
        ResponseEntity.noContent().build();
    }

    @GetMapping("/{lineId}/stations")
    public ResponseEntity<List<StationResponse>> findAllByLine(@PathVariable("lineId") Long lineId) {
        return ResponseEntity.ok(sectionService.findAllByLine(lineId));
    }
}
