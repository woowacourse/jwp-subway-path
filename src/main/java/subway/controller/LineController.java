package subway.controller;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.line.LineRequest;
import subway.dto.line.LineResponse;
import subway.dto.station.LineMapResponse;
import subway.service.LineService;
import subway.service.SubwayMapService;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final SubwayMapService subwayMapService;

    public LineController(final LineService lineService, final SubwayMapService subwayMapService) {
        this.lineService = lineService;
        this.subwayMapService = subwayMapService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody @Valid LineRequest lineRequest) {
        Long id = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findAll());
    }

    @GetMapping("/{lineNumber}")
    public ResponseEntity<LineMapResponse> findLineById(@PathVariable final Long lineNumber) {
        return ResponseEntity.ok().body(subwayMapService.showLineMap(lineNumber));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeLine(@PathVariable final Long id) {
        lineService.removeLineById(id);
        return ResponseEntity.noContent().build();
    }
}
