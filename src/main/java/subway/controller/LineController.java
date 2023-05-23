package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.request.CreateSectionRequest;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.service.LineService;
import subway.service.SectionService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;

    public LineController(final LineService lineService, final SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody final LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findAllLine());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable final Long id) {
        return ResponseEntity.ok(lineService.findLineById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable final Long id, @RequestBody final LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/stations")
    public ResponseEntity<LineResponse> createSection(@PathVariable Long id,
                                                      @RequestBody CreateSectionRequest createSectionRequest) {
        LineResponse line = sectionService.createSection(id, createSectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).build();
    }

    @DeleteMapping("/{id}/stations/{stationId}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @PathVariable Long stationId) {
        sectionService.deleteSection(id, stationId);
        return ResponseEntity.noContent().build();
    }
}
