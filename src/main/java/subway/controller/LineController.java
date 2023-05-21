package subway.controller;

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
import subway.controller.dto.LineRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.SectionCreateRequest;
import subway.controller.dto.SectionDeleteRequest;
import subway.domain.line.Line;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid LineRequest lineRequest) {
        Line line = lineService.saveLine(lineRequest);
        LineResponse lineResponse = LineResponse.of(line);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<Line> lines = lineService.findLineResponses();
        List<LineResponse> lineResponses = lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lineResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        Line line = lineService.findLineResponseById(id);
        LineResponse lineResponse = LineResponse.of(line);
        return ResponseEntity.ok(lineResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> createSection(@PathVariable Long id,
                                              @RequestBody @Valid SectionCreateRequest request) {
        lineService.createSection(id, request);
        return ResponseEntity.created(URI.create("/sections")).build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id,
                                              @RequestBody @Valid SectionDeleteRequest request) {
        lineService.deleteSection(id, request);
        return ResponseEntity.noContent().build();
    }
}
