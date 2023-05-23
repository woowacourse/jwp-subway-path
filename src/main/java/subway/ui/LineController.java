package subway.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.application.PathService;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineSaveResponse;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.dto.SectionAddRequest;
import subway.dto.StationDeleteRequest;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final PathService pathService;

    public LineController(LineService lineService, PathService pathService) {
        this.lineService = lineService;
        this.pathService = pathService;
    }

    @PostMapping
    public ResponseEntity<LineSaveResponse> createLine(@RequestBody @Valid LineRequest lineRequest) {
        Line line = lineService.saveLine(lineRequest);
        LineSaveResponse response = new LineSaveResponse(line.getId());
        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<Line> lines = lineService.findAllLines();
        List<LineResponse> response = lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> findLine(@PathVariable Long lineId) {
        Line line = lineService.findLineById(lineId);
        LineResponse response = LineResponse.from(line);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> addSection(@PathVariable Long lineId, @RequestBody @Valid SectionAddRequest request) {
        lineService.addSection(lineId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteSections(@PathVariable Long lineId,
                                               @RequestBody @Valid StationDeleteRequest request) {
        lineService.deleteStation(lineId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestBody @Valid PathRequest request) {
        PathResponse shortestPath = pathService.findShortestPath(request);
        return ResponseEntity.ok(shortestPath);
    }
}
