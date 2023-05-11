package subway.ui;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.business.service.LineService;
import subway.business.service.dto.LineResponse;
import subway.business.service.dto.LineSaveRequest;
import subway.business.service.dto.LineStationsResponse;
import subway.business.service.dto.StationAddToLineRequest;
import subway.ui.dto.StationDeleteRequest;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody LineSaveRequest lineSaveRequest) {
        LineResponse lineResponse = lineService.createLine(lineSaveRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).build();
    }

    @PostMapping("/{lineId}/station")
    public ResponseEntity<Void> addStationToLine(@NonNull @PathVariable Long lineId,
                                                 @RequestBody StationAddToLineRequest stationAddToLineRequest) {
        lineService.addStationToLine(lineId, stationAddToLineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/station")
    public ResponseEntity<Void> deleteStation(@PathVariable Long lineId,
                                              @RequestBody StationDeleteRequest stationDeleteRequest) {
        lineService.deleteStation(lineId, stationDeleteRequest.getStation());
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
