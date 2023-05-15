package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.*;
import subway.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest lineCreateRequest) {
        final LineResponse lineResponse = lineService.createLine(lineCreateRequest);
        return ResponseEntity
                .created(URI.create("/lines/" + lineResponse.getId()))
                .body(lineResponse);
    }

    @DeleteMapping("/lines/{lineId}")
    public ResponseEntity<String> deleteLine(@PathVariable Long lineId) {
        String lineName = lineService.deleteLine(lineId);
        return ResponseEntity
                .ok()
                .body(lineName);
    }

    @GetMapping("/lines/{lineId}")
    public ResponseEntity<LineResponse> findLine(@PathVariable Long lineId) {
        LineResponse lineResponse = lineService.findLine(lineId);
        return ResponseEntity
                .ok()
                .body(lineResponse);
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<LineResponse> lineResponses = lineService.findAllLines();
        return ResponseEntity
                .ok()
                .body(lineResponses);
    }

}
