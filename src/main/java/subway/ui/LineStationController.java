package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineStationService;
import subway.dto.request.LineStationRequest;
import subway.dto.response.LineStationResponse;

@RestController
@RequestMapping("/lines/{line_id}/stations")
public class LineStationController {
    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PostMapping
    ResponseEntity<LineStationResponse> createLineStation(@PathVariable("line_id") Long lineId, @RequestBody LineStationRequest lineStationRequest) {
        String upBoundStationName = lineStationRequest.getUpBoundStationName();
        String downBoundStationName = lineStationRequest.getDownBoundStationName();
        int distance = lineStationRequest.getDistance();
        LineStationResponse lineStationResponse = lineStationService.addStationToLine(upBoundStationName, downBoundStationName, distance, lineId);
        return ResponseEntity.status(HttpStatus.CREATED).body(lineStationResponse);
    }

    @DeleteMapping("/{station_id}")
    ResponseEntity<Void> deleteLineStation(@PathVariable("line_id") Long lineId, @PathVariable("station_id") Long stationId) {
        lineStationService.removeStationToLine(stationId, lineId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
