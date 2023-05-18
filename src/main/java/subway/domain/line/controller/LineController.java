package subway.domain.line.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.line.dto.LineRequest;
import subway.domain.line.dto.LineResponse;
import subway.domain.line.entity.LineEntity;
import subway.domain.line.service.LineService;
import subway.global.common.ResultResponse;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/line")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<ResultResponse> createLine(@RequestBody @Valid final LineRequest lineRequest) {
        LineEntity lineEntity = lineService.saveLine(lineRequest);
        LineResponse lineResponse = LineResponse.of(lineEntity);
        return ResponseEntity.created(URI.create("/line/" + lineResponse.getId())).body(ResultResponse.of(HttpStatus.CREATED, lineResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse> updateLine(@PathVariable final Long id, @RequestBody @Valid final LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().body(ResultResponse.of(HttpStatus.OK, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse> deleteLine(@PathVariable final Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResultResponse.of(HttpStatus.NO_CONTENT, id));
    }
}
