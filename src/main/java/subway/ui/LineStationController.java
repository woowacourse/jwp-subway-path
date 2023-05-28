package subway.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineStationService;
import subway.dto.LineStationResponse;
import subway.dto.LineStationRequest;

import javax.validation.Valid;
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
    public ResponseEntity<Void> createLinesStations(
            @PathVariable final Long lineId,
            @RequestBody @Valid final LineStationRequest request
    ) {
        lineStationService.saveLinesStations(lineId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/lines/" + lineId))
                .build();
    }

    @GetMapping("/stations")
    public ResponseEntity<List<LineStationResponse>> findAllLinesStations() {
        final List<LineStationResponse> findAllLines = lineStationService.findAllLinesStations();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(findAllLines);
    }

    @GetMapping("/{lineId}/stations")
    public ResponseEntity<LineStationResponse> findLineStations(
            @PathVariable final Long lineId
    ) {
        final LineStationResponse lineStationResponse = lineStationService.findLinesStations(lineId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lineStationResponse);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteLineStations(
            @PathVariable final Long lineId,
            @PathVariable final Long stationId
    ) {
        lineStationService.deleteLinesStations(lineId, stationId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
