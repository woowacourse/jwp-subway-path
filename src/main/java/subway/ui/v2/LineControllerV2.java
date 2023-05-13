package subway.ui.v2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.request.CreateLineRequest;
import subway.application.response.LineResponse;
import subway.application.v2.LineServiceV2;

import java.net.URI;
import java.util.List;

@RequestMapping("/v2/lines")
@RestController
public class LineControllerV2 {

    private final LineServiceV2 lineService;

    public LineControllerV2(final LineServiceV2 lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Long> createLine(@RequestBody CreateLineRequest request) {
        final Long saveLineId = lineService.saveLine(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/v2/lines/" + saveLineId))
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
