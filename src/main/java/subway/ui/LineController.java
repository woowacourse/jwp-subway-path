package subway.ui;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineSelectResponse;
import subway.dto.LinesSelectResponse;
import subway.dto.StationSaveRequest;
import subway.dto.StationSelectResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    private LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<StationSelectResponse> createStation(@PathVariable Long lineId,
                                                               @RequestBody StationSaveRequest stationSaveRequest) {
        StationSelectResponse response = lineService.addStation(lineId, stationSaveRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<LinesSelectResponse> findAllLines() {
        return ResponseEntity.ok(lineService.findAllLine());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineSelectResponse> findLineById(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.getStationsByLineId(lineId));
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.deleteStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

}
