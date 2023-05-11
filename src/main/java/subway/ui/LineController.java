package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.application.SectionService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineStationRequest;
import subway.dto.LineStationResponse;

import java.net.URI;
import java.sql.SQLException;
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

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Void> addStation(@PathVariable Long lineId, @RequestBody LineStationRequest lineStationRequest) {
        Long id = sectionService.addStation(lineId, lineStationRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/" + id)).build();
    }

    @GetMapping("/{lineId}/stations")
    public ResponseEntity<LineStationResponse> getLineStations(@PathVariable Long lineId) {
        LineStationResponse lineStationResponse = sectionService.findByLineId(lineId);
        return ResponseEntity.ok().body(lineStationResponse);
    }

    @DeleteMapping("/{lineId}/stations/{station_id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        sectionService.removeStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }


}
