package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineStationResponse;

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
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.save(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineStationResponse>> findAllLines() {
        return ResponseEntity.ok().body(lineService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineStationResponse> findLineById(@PathVariable Long id) {
        LineStationResponse lineStationResponse = lineService.findById(id);
        return ResponseEntity.ok(lineStationResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
        lineService.update(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleSQLException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.badRequest().build();
    }
}
