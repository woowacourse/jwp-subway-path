package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineStationService;
import subway.dto.ConnectEndpointDto;
import subway.dto.ConnectRequest;
import subway.dto.InitConnectDto;

@RestController
@RequestMapping("/lines/{lineId}/stations")
public class LineStationController {
    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PatchMapping("/{stationId}")
    public ResponseEntity<Void> connectStations(@PathVariable Long lineId, @PathVariable Long stationId, @RequestParam String type, @RequestBody ConnectRequest request) {
        if (type.equals("init")) {
            lineStationService.connectInitStations(InitConnectDto.of(lineId, stationId, request));
        }
        if (type.equals("up")) {
            lineStationService.connectUpEndpoint(ConnectEndpointDto.of(lineId, stationId, request));
        }
        if (type.equals("down")) {
            lineStationService.connectDownEndpoint(ConnectEndpointDto.of(lineId, stationId, request));
        }
        if (type.equals("mid")) {
            lineStationService.connectMidStation(lineId, stationId, request);
        }
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }
}
