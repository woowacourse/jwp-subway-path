package subway.ui.v2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.request.CreateLineRequest;
import subway.application.v2.LineServiceV2;
import subway.dto.LineResponse;

import java.net.URI;

@RequestMapping("/v2/lines")
@RestController
public class LineControllerV2 {

    private final LineServiceV2 lineServiceV2;

    public LineControllerV2(final LineServiceV2 lineServiceV2) {
        this.lineServiceV2 = lineServiceV2;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody CreateLineRequest request) {
        final Long saveLineId = lineServiceV2.saveLine(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/v2/lines/" + saveLineId))
                .build();
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> findLineByLineId(@PathVariable Long lineId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }
}
