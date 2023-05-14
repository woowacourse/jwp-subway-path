package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineStationService;
import subway.dto.CreateType;
import subway.dto.request.ConnectRequest;

@RestController
@RequestMapping("/lines/{lineId}/stations")
public class LineStationController {
    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PatchMapping("/{stationId}")
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

}
