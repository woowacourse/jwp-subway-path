package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.dto.LineFindResponse;

import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<List<LineFindResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findAllLineStationNames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineFindResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findStationNamesByLineId(id));
    }
}
