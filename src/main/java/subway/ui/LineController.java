package subway.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
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
import subway.domain.Line;
import subway.ui.dto.line.LineCreateRequest;
import subway.ui.dto.line.LineResponse;
import subway.ui.dto.line.LineUpdateRequest;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(
        @RequestBody @Valid LineCreateRequest lineRequest) {
        LineResponse lineResponse = LineResponse.from(lineService.saveLine(lineRequest));
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId()))
            .body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<LineResponse> lineResponses = mapToLineResponse(lineService.findLines());
        return ResponseEntity.ok(lineResponses);
    }

    private List<LineResponse> mapToLineResponse(List<Line> lines) {
        return lines.stream()
            .map(LineResponse::from)
            .collect(toList());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long lineId) {
        LineResponse lineResponse = LineResponse.from(lineService.findLineById(lineId));
        return ResponseEntity.ok(lineResponse);
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<LineResponse> updateLine(@PathVariable Long lineId,
        @RequestBody @Valid LineUpdateRequest lineUpdateRequest) {
        LineResponse lineResponse = LineResponse.from(
            lineService.updateLine(lineUpdateRequest, lineId));
        return ResponseEntity.ok().body(lineResponse);
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }
}
