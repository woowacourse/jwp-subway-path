package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.service.LineService;

import java.net.URI;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest request) {
        LineResponse line = lineService.addLine(request);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    //TODO: 주석해제
//    @GetMapping("/lines")
//    public ResponseEntity<List<LineResponse>> findAllLines() {
//        return ResponseEntity.ok(lineService.findLineResponses());
//    }


//    @GetMapping("/{id}")
//    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
//        return ResponseEntity.ok(lineService.findLineResponseById(id));
//    }
//
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
