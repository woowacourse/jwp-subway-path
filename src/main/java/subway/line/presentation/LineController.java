package subway.line.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.domain.section.dto.SectionRequest;
import subway.line.domain.section.dto.SectionResponse;
import subway.line.domain.section.dto.SectionSavingRequest;
import subway.line.presentation.dto.LineRequest;
import subway.line.presentation.dto.LineResponse;
import subway.line.presentation.dto.StationDeletingRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineServicePort lineServicePort;

    public LineController(LineServicePort lineServicePort) {
        this.lineServicePort = lineServicePort;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse lineResponse = lineServicePort.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineServicePort.findLineResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineServicePort.findLineResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineServicePort.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineServicePort.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/section")
    public ResponseEntity<Void> insertSection(@PathVariable long lineId, @RequestBody SectionSavingRequest sectionSavingRequest) {
        long savedId = lineServicePort.saveSection(lineId, sectionSavingRequest);
        return ResponseEntity.created(URI.create(String.format("/lines/%d/%d", lineId, savedId))).build();
    }

    @DeleteMapping("/{lineId}/section")
    public ResponseEntity<Void> deleteStation(@PathVariable long lineId, @RequestBody StationDeletingRequest stationRequest) {
        lineServicePort.deleteStation(lineId, stationRequest.getStationId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/section")
    public ResponseEntity<SectionResponse> findShortestPath(@RequestBody SectionRequest sectionRequest) {
        final var shortestPath = lineServicePort.findShortestPath(sectionRequest.getStartingStationId(), sectionRequest.getDestinationStationId());
        final var fare = lineServicePort.calculateFare(shortestPath.getShortestDistance());
        return ResponseEntity.ok().body(SectionResponse.of(shortestPath, fare));
    }
}
