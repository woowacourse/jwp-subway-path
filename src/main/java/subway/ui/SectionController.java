package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import subway.ui.dto.response.LineResponse;
import subway.ui.dto.request.SectionRequest;
import subway.application.SectionService;
import subway.ui.dto.response.SectionResponse;
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
    public ResponseEntity<SectionResponse> createSection(@RequestBody SectionRequest sectionRequest) {
        final SectionResponse response = sectionService.createSection(sectionRequest);
        final URI uri = URI.create("/sections");
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<List<StationResponse>> findAllByLine(@PathVariable(name = "lineId") long lineId) {
        final List<StationResponse> stations = sectionService.findAllByLine(lineId);
        return ResponseEntity.ok(stations);
    }

    @DeleteMapping("/{lineId}/stations")
    public void deleteSection(@PathVariable(name = "lineId") Long lineId, @RequestBody LineResponse.SectionDeleteRequest sectionDeleteRequest) {
        sectionService.deleteSection(lineId, sectionDeleteRequest);
        ResponseEntity.noContent().build();
    }
}
