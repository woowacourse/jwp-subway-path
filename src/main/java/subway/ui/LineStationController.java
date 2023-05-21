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
import subway.dto.request.ConnectionEndpointRequest;
import subway.dto.request.ConnectionInitRequest;
import subway.dto.request.ConnectionMidRequest;
import subway.dto.response.LineStationResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineStationController {
    private final LineStationService lineStationService;

    public LineStationController(final LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PatchMapping("/{lineId}/stations/{stationId}/init")
    public ResponseEntity<Void> addInitStation(
            @PathVariable final Long lineId,
            @PathVariable final Long stationId,
            @RequestBody @Valid final ConnectionInitRequest request) {
        lineStationService.addInitStations(lineId, stationId, request.getNextStationId(), request.getDistance());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{lineId}/stations/{stationId}/endpoint")
    public ResponseEntity<Void> addEndpointStation(
            @PathVariable final Long lineId,
            @PathVariable final Long stationId,
            @RequestBody @Valid final ConnectionEndpointRequest request) {
        lineStationService.addEndpoint(request.getEndpointType(), lineId, stationId, request.getDistance());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{lineId}/stations/{stationId}/mid")
    public ResponseEntity<Void> addMidStation(
            @PathVariable final Long lineId,
            @PathVariable final Long stationId,
            @RequestBody @Valid final ConnectionMidRequest request) {
        lineStationService.addIntermediate(lineId, stationId, request.getPrevStationId(), request.getDistance());
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStationById(
            @PathVariable final Long lineId,
            @PathVariable final Long stationId) {
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
