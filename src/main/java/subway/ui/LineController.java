package subway.ui;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<LineResponse> lines = lineService.findLines().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lines);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long lineId) {
        LineResponse line = LineResponse.of(lineService.findLineById(lineId));
        return ResponseEntity.ok(line);
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest request) {
        LineResponse line = new LineResponse(lineService.createLine(request),
                request.getName(), request.getColor(), Collections.emptyList());
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<Void> updateLine(@PathVariable Long lineId, @RequestBody LineRequest lineUpdateRequest) {
        lineService.updateLineInfo(lineId, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }
}
