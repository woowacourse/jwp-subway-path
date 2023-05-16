package subway.domain.lineDetail.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.domain.lineDetail.dto.LineDetailRequest;
import subway.domain.lineDetail.dto.LineDetailResponse;
import subway.domain.lineDetail.service.LineDetailService;
import subway.global.common.ResultResponse;

import java.net.URI;

@RestController
@RequestMapping("/line-detail")
public class LineDetailController {

    private final LineDetailService lineDetailService;

    public LineDetailController(final LineDetailService lineDetailService) {
        this.lineDetailService = lineDetailService;
    }

    @PostMapping
    public ResponseEntity<ResultResponse> createLine(@RequestBody final LineDetailRequest lineDetailRequest) {
        final LineDetailResponse line = lineDetailService.saveLine(lineDetailRequest);
        return ResponseEntity.created(URI.create("/line-detail/" + line.getId())).body(new ResultResponse(201, "노선 추가 성공", line));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse> updateLine(@PathVariable final Long id, @RequestBody final LineDetailRequest lineUpdateRequest) {
        lineDetailService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().body(new ResultResponse(200, "노선 업데이트 성공", id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse> deleteLine(@PathVariable final Long id) {
        lineDetailService.deleteLineById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResultResponse(204, "노선 삭제 성공", id));
    }
}
