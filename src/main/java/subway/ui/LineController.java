package subway.ui;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.Line;
import subway.dto.AddStationToLineRequest;
import subway.dto.LineCreateRequest;
import subway.dto.LineCreateResponse;
import subway.service.LineService;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineCreateResponse> createLine(@RequestBody LineCreateRequest createRequest) {
        Line newLine = lineService.createNewLine(createRequest);
        LineCreateResponse lineCreateResponse = new LineCreateResponse(newLine.getId(), newLine.getName());
        return ResponseEntity.created(URI.create("/lines/" + newLine.getId())).body(lineCreateResponse);
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Void> addStationToLine(@PathVariable Long lineId,
                                                 @RequestBody AddStationToLineRequest addStationToLineRequest) {
        lineService.addStationToExistLine(lineId, addStationToLineRequest);
        return ResponseEntity.ok().build();
    }

    /*
    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.findLineResponseById(lineId));
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> delete(@PathVariable Long lineId,
                                       @PathVariable Long stationId) {
        // TODO
        return ResponseEntity.noContent().build();
    }*/
}
