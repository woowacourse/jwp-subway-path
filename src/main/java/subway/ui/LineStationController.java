package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineStationService;
import subway.dto.request.ConnectionRequest;
import subway.dto.response.LineStationResponse;

import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineStationController {
    private final LineStationService lineStationService;

    public LineStationController(final LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PatchMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> addStationToLine(@PathVariable final Long lineId, @PathVariable final Long stationId, @RequestBody final ConnectionRequest request) {
        if (ConnectionType.INIT == ConnectionType.from(request.getConnectionType())) {
            lineStationService.addInitStations(lineId, stationId, request.getNextStationId(), request.getDistance());
        }
        if (ConnectionType.UP == ConnectionType.from(request.getConnectionType())) {
            lineStationService.addUpEndpoint(lineId, stationId, request.getDistance());
        }
        if (ConnectionType.DOWN == ConnectionType.from(request.getConnectionType())) {
            lineStationService.addDownEndpoint(lineId, stationId, request.getDistance());
        }
        if (ConnectionType.MID == ConnectionType.from(request.getConnectionType())) {
            lineStationService.addIntermediate(lineId, stationId, request.getPrevStationId(), request.getDistance());
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStationById(@PathVariable final Long lineId, @PathVariable final Long stationId) {
        lineStationService.deleteStationInLine(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{lineId}/stations")
    public ResponseEntity<LineStationResponse> showStationsByLineId(@PathVariable final Long lineId) {
        return ResponseEntity.ok().body(lineStationService.findByLineId(lineId));
    }

    @GetMapping("/stations")
    public ResponseEntity<List<LineStationResponse>> showStations() {
        return ResponseEntity.ok().body(lineStationService.findAll());
    }
}
