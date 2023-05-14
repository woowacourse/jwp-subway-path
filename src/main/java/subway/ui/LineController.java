package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.application.SectionService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionSavingRequest;
import subway.dto.StationRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;


    public LineController(LineService lineService, SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/section")
    public ResponseEntity<Void> initializeSection(@PathVariable long lineId, @RequestBody SectionSavingRequest sectionSavingRequest) {
        long savedId = sectionService.insert(lineId, sectionSavingRequest.getPreviousStationName(),
                sectionSavingRequest.getNextStationName(), sectionSavingRequest.getDistance(), sectionSavingRequest.isDown());
        return ResponseEntity.created(URI.create(String.format("/lines/%d/%d", lineId, savedId))).build();
    }

    @DeleteMapping("/{lineId}/section")
    public ResponseEntity<Void> deleteSection(@PathVariable long lineId, @RequestBody StationRequest stationRequest) {
        sectionService.deleteStation(lineId, stationRequest.getName());
        return ResponseEntity.ok().build();
    }
}
