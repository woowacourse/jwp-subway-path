package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

import java.net.URI;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<LineResponse, List<StationResponse>>> findAllLines() {
        LineResponse second = LineResponse.of(new Line("2호선", "green"));
        StationResponse 잠실역 = StationResponse.of(new Station("잠실역"));
        StationResponse 선릉역 = StationResponse.of(new Station("선릉역"));
        Map<LineResponse, List<StationResponse>> response = new HashMap<>();
        response.put(second, List.of(잠실역, 선릉역));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<StationResponse>> findLineById(@PathVariable Long id) {
        StationResponse 잠실역 = StationResponse.of(new Station("잠실역"));
        StationResponse 선릉역 = StationResponse.of(new Station("선릉역"));
        return ResponseEntity.ok(List.of(잠실역, 선릉역));
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
