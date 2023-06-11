package subway.controller.line;

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
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineResponse;
import subway.dto.station.LineMapResponse;
import subway.service.line.LineMapService;
import subway.service.line.LineService;

@RequestMapping("/lines")
@RestController
public class LineController {

    private final LineService lineService;
    private final LineMapService lineMapService;

    public LineController(final LineService lineService, final LineMapService lineMapService) {
        this.lineService = lineService;
        this.lineMapService = lineMapService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody @Valid LineCreateRequest lineCreateRequest) {
        Long id = lineService.createLine(lineCreateRequest);
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeLine(@PathVariable final Long id) {
        lineService.removeLine(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAll() {
        return ResponseEntity.ok(lineService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineMapResponse> findById(@PathVariable final Long id) {
        return ResponseEntity.ok().body(lineMapService.findById(id));
    }
}
