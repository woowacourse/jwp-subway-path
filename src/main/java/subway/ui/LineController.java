package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

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
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> readAllLine() {
        List<LineResponse> lines = lineService.readAllLine();
        return ResponseEntity.ok(lines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> readLine(@PathVariable final Long id) {
        LineResponse line = lineService.readLine(id);
        return ResponseEntity.ok(line);
    }
}
