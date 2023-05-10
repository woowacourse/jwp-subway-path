package subway.ui;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.dto.LineResponse;
import subway.dto.LineSaveRequest;
import subway.dto.LineStationsResponse;
import subway.dto.StationAddToLineRequest;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody LineSaveRequest lineSaveRequest) {
        LineResponse lineResponse = lineService.saveLine(lineSaveRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).build();
    }

    @PostMapping("/{lineId}/station")
    public ResponseEntity<Void> addStationToLine(@PathVariable Long lineId,
                                                 @RequestBody StationAddToLineRequest stationAddToLineRequest) {
        lineService.addStationToLine(stationAddToLineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.deleteStation(lineId, stationId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{lineId}")
    public ResponseEntity<LineStationsResponse> findLineById(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.findLineResponseById(lineId));
    }

    @GetMapping
    public ResponseEntity<List<LineStationsResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }
}
