package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.dto.LineResponse;

import java.util.List;
import subway.dto.StationResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }


//    @PostMapping
//    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
//        LineResponse line = lineService.saveLine(lineRequest);
//        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
//    }

    /**
     * 노선도에 있는 모든 노선들의 정보를 조회
     * @return
     */
    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    /**
     * 특정 노선의 정보 조회
     * @param lineId
     * @return
     */
    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.findLineResponseById(lineId));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
//        lineService.updateLine(id, lineUpdateRequest);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
//        lineService.deleteLineById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @ExceptionHandler(SQLException.class)
//    public ResponseEntity<Void> handleSQLException() {
//        return ResponseEntity.badRequest().build();
//    }
}
