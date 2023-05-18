package subway.domain.line.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.line.dto.LineRequest;
import subway.domain.line.dto.LineResponse;
import subway.domain.line.entity.LineEntity;
import subway.domain.line.service.LineService;
import subway.global.common.ResultResponse;

import java.net.URI;

@RestController
@RequestMapping("/line-detail")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<ResultResponse> createLine(@RequestBody final LineRequest lineRequest) {
        LineEntity lineEntity = lineService.saveLine(lineRequest);
        LineResponse lineResponse = LineResponse.of(lineEntity);
        return ResponseEntity.created(URI.create("/line-detail/" + lineResponse.getId())).body(new ResultResponse(201, "노선 추가 성공", lineResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse> updateLine(@PathVariable final Long id, @RequestBody final LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().body(new ResultResponse(200, "노선 업데이트 성공", id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse> deleteLine(@PathVariable final Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResultResponse(204, "노선 삭제 성공", id));
    }
}
