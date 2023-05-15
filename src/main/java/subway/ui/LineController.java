package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.PathRequest;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public final class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid final LineRequest lineRequest) {
        final LineResponse line = lineService.saveLine(lineRequest);

        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable final Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable final Long id,
                                           @RequestBody @Valid final LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long id) {
        lineService.deleteLineById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/stations")
    public ResponseEntity<Void> addPathToLine(@PathVariable final Long id,
                                              @RequestBody @Valid final PathRequest pathRequest) {
        lineService.addPathToLine(id, pathRequest);

        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @DeleteMapping("/{id}/stations/{station-id}")
    public ResponseEntity<Void> deleteStationFromLine(@PathVariable final Long id,
                                                      @PathVariable("station-id") final Long stationId) {
        lineService.deletePathByStationId(stationId);

        return ResponseEntity.noContent().build();
    }
}
