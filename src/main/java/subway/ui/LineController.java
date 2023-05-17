package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.dto.LineAndStationsResponse;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationAddRequest;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/stations")
    public ResponseEntity<List<LineAndStationsResponse>> findAllLineStations() {
        return ResponseEntity.ok(lineService.findLines());
    }

    @GetMapping("/{lineId}/stations")
    public ResponseEntity<LineAndStationsResponse> findStationsByLineId(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.findLineById(lineId));
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Void> addStationToLine(@PathVariable Long lineId, @RequestBody StationAddRequest stationAddRequest) {
        lineService.addStationToLine(lineId, stationAddRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStationFromLineByIds(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.deleteStationFromLine(lineId, stationId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(final Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(final Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
