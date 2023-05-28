package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.dto.LineFindResponse;
import subway.dto.LineRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody LineRequest lineRequest) {
        Long lineId = lineService.saveLine(lineRequest);
        System.out.println("asdfasdfasdfasdfa" + lineId);
        return ResponseEntity
                .created(URI.create("/lines/" + lineId))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<LineFindResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findAllLineStationNames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineFindResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findStationNamesByLineId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
