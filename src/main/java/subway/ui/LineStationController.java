package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineStationService;
import subway.dto.LineStationResponse;
import subway.dto.LineStationsRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineStationController {

    private final LineStationService lineStationService;

    public LineStationController(final LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Void> createLinesStations(@PathVariable Long lineId, @RequestBody LineStationsRequest request) {
        lineStationService.saveSection(lineId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/lines/"+lineId))
                .build();
    }

    @GetMapping("/stations")
    public ResponseEntity<List<LineStationResponse>> findAllLinesStations() {
        final List<LineStationResponse> findAllLines = lineStationService.findAllLines();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(findAllLines);
    }

    @GetMapping("/{lineId}/stations")
    public ResponseEntity<LineStationResponse> findLineStations(@PathVariable Long lineId) {
        final LineStationResponse lineStationResponse = lineStationService.findStations(lineId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lineStationResponse);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteLineStations(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineStationService.deleteStation(lineId, stationId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
