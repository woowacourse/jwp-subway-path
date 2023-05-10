package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.application.SubwayMapService;
import subway.domain.entity.LineEntity;
import subway.domain.entity.StationEntity;
import subway.dto.line.LineRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final SubwayMapService subwayMapService;

    public LineController(LineService lineService, final SubwayMapService subwayMapService) {
        this.lineService = lineService;
        this.subwayMapService = subwayMapService;
    }

    @PostMapping
    public ResponseEntity<Long> createLine(@RequestBody LineRequest lineRequest) {
        Long id = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + id)).body(id);
    }

    @GetMapping
    public ResponseEntity<List<LineEntity>> findAllLines() {
        return ResponseEntity.ok(lineService.findAll());
    }

    @GetMapping("/{lineNumber}")
    public ResponseEntity<List<StationEntity>> findLineById(@PathVariable final Long lineNumber) {
        return ResponseEntity.ok().body(subwayMapService.showLineMap(lineNumber));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable final Long id, @RequestBody final LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
