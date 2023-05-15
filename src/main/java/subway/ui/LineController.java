package subway.ui;

import java.net.URI;
import java.util.List;
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
import subway.application.RouteService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationsByLineResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final RouteService routeService;

    public LineController(LineService lineService, final RouteService routeService) {
        this.lineService = lineService;
        this.routeService = routeService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<StationsByLineResponse>> findAllLines() {
        return ResponseEntity.ok(routeService.findAllStationsByLineResponses());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<StationsByLineResponse> findLineById(@PathVariable Long lineId) {
        return ResponseEntity.ok(routeService.findStationByLineResponseById(lineId));
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

}
