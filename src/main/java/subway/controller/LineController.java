package subway.controller;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.LinesResponse;
import subway.controller.dto.SectionCreateRequest;
import subway.service.LineService;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@Valid @RequestBody LineCreateRequest request) {
        final Long lineId = lineService.createLine(request);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLine(@PathVariable(name = "id") Long lineId) {
        final LineResponse response = lineService.findLineById(lineId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<LinesResponse> findLines() {
        final LinesResponse response = lineService.findLines();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> createSection(
            @PathVariable(name = "id") Long lineId,
            @Valid @RequestBody SectionCreateRequest request
    ) {
        lineService.createSection(lineId, request);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable(name = "id") Long lineId,
                                              @RequestParam Long stationId) {
        lineService.deleteStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
