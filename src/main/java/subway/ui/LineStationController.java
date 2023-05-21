package subway.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineStationService;
import subway.dto.LineStationAddRequest;
import subway.dto.LineStationInitRequest;
import subway.dto.StationRequest;

@RestController
@RequestMapping("/lines")
public class LineStationController {

    private final LineStationService lineStationService;

    public LineStationController(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @PostMapping("/{lineId}/stations/init")
    public ResponseEntity<Void> initStationsToLine (@PathVariable Long lineId, @RequestBody @Valid LineStationInitRequest lineStationInitRequest) {
        lineStationService.initStations(lineId, lineStationInitRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Void> addStationToLine (@PathVariable Long lineId, @RequestBody @Valid LineStationAddRequest lineStationAddRequest) {
        lineStationService.addStation(lineId, lineStationAddRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/stations/")
    public ResponseEntity<Void> deleteStationInLine (@PathVariable Long lineId, @RequestBody @Valid StationRequest request) {
        lineStationService.removeStation(lineId, request);
        return ResponseEntity.noContent().build();
    }

}
