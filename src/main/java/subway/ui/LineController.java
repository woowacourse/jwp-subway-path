package subway.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SaveResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<SaveResponse> createLine(@RequestBody @Valid LineRequest lineRequest) {
        SaveResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> findLine(@PathVariable Long lineId) {
        LineResponse response = lineService.findLineById(lineId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<LineResponse> lines = lineService.findAllLines();
        return ResponseEntity.ok(lines);
    }
}
