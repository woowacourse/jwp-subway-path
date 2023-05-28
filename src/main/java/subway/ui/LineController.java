package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(
            @RequestBody @Valid final LineRequest lineRequest
    ) {
        Long savedId = lineService.saveLine(lineRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/lines/" + savedId))
                .build();
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> readLine(
            @PathVariable final Long lineId
    ) {
        LineResponse line = lineService.findLine(lineId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(line);
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<Void> updateLine(
            @PathVariable final Long lineId,
            @RequestBody @Valid final LineRequest request
    ) {
        lineService.editLine(lineId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/lines/" + lineId))
                .build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(
            @PathVariable final Long lineId
    ) {
        lineService.deleteLine(lineId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
