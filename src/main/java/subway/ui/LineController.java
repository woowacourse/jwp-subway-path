package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.domain.Line;
import subway.dto.*;
import subway.service.LineCreateService;
import subway.service.LineCreateServiceImpl;
import subway.service.LineFindService;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    private final LineCreateService lineCreateService;
    private final LineFindService lineFindService;

    public LineController(LineService lineService, final LineCreateServiceImpl lineCreateService, final LineFindService lineFindService) {
        this.lineService = lineService;
        this.lineCreateService = lineCreateService;
        this.lineFindService = lineFindService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest lineRequest) {
        final LineCreateDto lineCreateDto = LineCreateDto.from(lineRequest);
        final Line line = lineCreateService.createLine(lineCreateDto);
        return ResponseEntity
                .created(URI.create("/lines/" + line.getId()))
                .body(LineResponse.of(line));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse3> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
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

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
