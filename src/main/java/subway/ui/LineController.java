package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.request.CreateLineRequest;
import subway.application.response.LineResponse;
import subway.application.LineService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequestMapping("/lines")
@RestController
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Long> createLine(@Valid @RequestBody CreateLineRequest request) {
        final Long saveLineId = lineService.saveLine(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/lines/" + saveLineId))
                .body(saveLineId);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> findLineByLineId(@PathVariable Long lineId) {
        final LineResponse lineResponse = lineService.findByLineId(lineId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAll() {
        final List<LineResponse> findLineResponses = lineService.findAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(findLineResponses);
    }
}
