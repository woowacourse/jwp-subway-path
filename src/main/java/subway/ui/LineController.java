package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.application.request.LineRequest;
import subway.application.request.SectionRequest;
import subway.application.request.StationRequest;
import subway.application.response.LineResponse;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody final LineRequest lineRequest) {
        final long createdId = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + createdId)).build();
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        final List<LineResponse> res = lineService.findLineResponses();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable final Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable final Long id, @Valid @RequestBody final LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/register")
    public ResponseEntity<Void> registerStation(@PathVariable final Long lineId, @Valid @RequestBody final SectionRequest request) {
        lineService.registerStation(lineId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{lineId}/unregister")
    public ResponseEntity<Void> unregisterStation(@PathVariable final Long lineId, @Valid @RequestBody final StationRequest request) {
        lineService.unregisterStation(lineId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
