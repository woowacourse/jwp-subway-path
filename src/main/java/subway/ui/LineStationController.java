package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineStationService;
import subway.dto.CreateType;
import subway.dto.request.ConnectRequest;
import subway.dto.response.LineStationResponse;

import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineStationController {
    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PatchMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> connectStations(@PathVariable Long lineId, @PathVariable Long stationId, @RequestParam String type, @RequestBody ConnectRequest request) {
        if (CreateType.INIT == CreateType.from(type)) {
            lineStationService.addInitStations(lineId, stationId, request.getNextStationId(), request.getDistance());
        }
        if (CreateType.UP == CreateType.from(type)) {
            lineStationService.addUpEndpoint(lineId, stationId, request.getDistance());
        }
        if (CreateType.DOWN == CreateType.from(type)) {
            lineStationService.addDownEndpoint(lineId, stationId, request.getDistance());
        }
        if (CreateType.MID == CreateType.from(type)) {
            lineStationService.addIntermediate(lineId, stationId, request.getPrevStationId(), request.getDistance());
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStationById(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineStationService.deleteStationById(lineId, stationId);
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
