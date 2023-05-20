package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.service.LineService;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest request) {
        LineResponse line = lineService.addLine(request);
        return ResponseEntity.created(URI.create("/lines" + line.getId())).body(line);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        LineResponse lineResponseById = lineService.createLineResponseById(id);
        return ResponseEntity.ok(lineResponseById);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Integer> updateLine(@PathVariable Long id, @RequestBody LineCreateRequest updateRequest) {
        lineService.updateLine(id, updateRequest);
        
        return ResponseEntity.ok().build();
    }

    //
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
//        lineService.deleteLineById(id);
//        return ResponseEntity.noContent().build();
//    }
//
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
