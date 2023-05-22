package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.dto.LineAndStationsResponse;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationAddRequest;

import java.net.URI;
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

    @GetMapping()
    public ResponseEntity<List<LineAndStationsResponse>> findAllLineStations() {
        return ResponseEntity.ok(lineService.findLines());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineAndStationsResponse> findStationsByLineId(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.findLineById(lineId));
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Void> addStationToLine(@PathVariable Long lineId, @RequestBody StationAddRequest stationAddRequest) {
        lineService.addStationToLine(lineId, stationAddRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStationFromLineByIds(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.deleteStationFromLine(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
