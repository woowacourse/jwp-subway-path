package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.Line;
import subway.dto.*;
import subway.service.LineCreateService;
import subway.service.LineFindService;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineCreateService lineCreateService;

    public LineController(LineCreateService lineCreateService) {
        this.lineCreateService = lineCreateService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineCreateRequest lineRequest) {
        final LineCreateDto lineCreateDto = LineCreateDto.from(lineRequest);
        final Line line = lineCreateService.createLine(lineCreateDto);
        return ResponseEntity
                .created(URI.create("/lines/" + line.getId()))
                .body(LineResponse.of(line));
    }
}
