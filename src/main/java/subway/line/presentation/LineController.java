package subway.line.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.application.LineService;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.section.dto.SectionRequest;
import subway.line.domain.section.dto.SectionResponse;
import subway.line.domain.section.dto.SectionSavingRequest;
import subway.line.domain.station.dto.StationRequest;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

//    @PostMapping
//    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
//        LineResponse line = lineService.saveLine(lineRequest);
//        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
//    }

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
    public ResponseEntity<Void> insertSection(@PathVariable long lineId, @RequestBody SectionSavingRequest sectionSavingRequest) {
        long savedId = lineService.saveSection(lineId, sectionSavingRequest.getPreviousStationName(),
                sectionSavingRequest.getNextStationName(), sectionSavingRequest.getDistance(), sectionSavingRequest.isDown());
        return ResponseEntity.created(URI.create(String.format("/lines/%d/%d", lineId, savedId))).build();
    }

    @DeleteMapping("/{lineId}/section")
    public ResponseEntity<Void> deleteStation(@PathVariable long lineId, @RequestBody StationRequest stationRequest) {
        lineService.deleteStation(null, stationRequest.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/section")
    public ResponseEntity<SectionResponse> findShortestPath(@RequestBody SectionRequest sectionRequest) {
        final var shortestPath = lineService.findShortestPath(sectionRequest.getStartingStation(), sectionRequest.getDestinationStation());
        final var fare = lineService.calculateFare(shortestPath.getShortestDistance());
        return ResponseEntity.ok().body(SectionResponse.of(shortestPath, fare));
    }
}
