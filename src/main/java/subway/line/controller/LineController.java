package subway.line.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.global.common.ResultResponse;
import subway.global.common.SuccessCode;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.service.LineService;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<ResultResponse> createLine(@RequestBody final LineRequest lineRequest) {
        final LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(new ResultResponse(SuccessCode.CREATE_LINE, line));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse> updateLine(@PathVariable final Long id, @RequestBody final LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().body(new ResultResponse(SuccessCode.UPDATE_LINE, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse> deleteLine(@PathVariable final Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResultResponse(SuccessCode.DELETE_LINE, id));
    }
}
