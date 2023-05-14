package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.Line;
import subway.dto.*;
import subway.service.LineCreateService;

import java.net.URI;

@RestController
public class LineController {

    private final LineCreateService lineCreateService;

    public LineController(LineCreateService lineCreateService) {
        this.lineCreateService = lineCreateService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest lineCreateRequest) {
        final LineResponse lineResponse = lineCreateService.createLine(lineCreateRequest);
        return ResponseEntity
                .created(URI.create("/lines/" + lineResponse.getId()))
                .body(lineResponse);
    }

}
