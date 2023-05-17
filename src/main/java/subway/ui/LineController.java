package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody LineRequest lineRequest) {
        Long savedId = lineService.save(lineRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/lines/"+savedId+"/stations"))
                .build();
    }

    @GetMapping("/stations")
    public ResponseEntity<List<LineResponse>> findAllLines() {
        final List<LineResponse> findAllLines = lineService.findAllLines();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(findAllLines);
    }

    @GetMapping("/{lineId}/stations")
    public ResponseEntity<LineResponse> findLineStations(@PathVariable Long lineId) {
        final LineResponse lineResponse = lineService.findStations(lineId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lineResponse);
    }
}
