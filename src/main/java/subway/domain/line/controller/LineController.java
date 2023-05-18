package subway.domain.line.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import subway.domain.line.domain.Line;
import subway.domain.line.domain.ShortestPath;
import subway.domain.line.dto.LineResponse;
import subway.domain.line.service.LineService;
import subway.global.common.ResultResponse;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/line")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<ResultResponse> findAllLine() {
        List<Line> lines = lineService.findAll();
        List<LineResponse> lineResponses = lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new ResultResponse(200, "전체 노선도 조회 성공", lineResponses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse> findLineById(@PathVariable final Long id) {
        Line line = lineService.findById(id);
        return ResponseEntity.ok().body(new ResultResponse(200, "단일 노선도 조회 성공", LineResponse.of(line)));
    }

    @GetMapping("/path")
    public ResponseEntity<ResultResponse> findPath(@RequestParam final Long startLineId, @RequestParam final Long endLineId) {
        ShortestPath shortestPath = lineService.findShortestPath(startLineId, endLineId);
        return ResponseEntity.ok().body(new ResultResponse(200, "최단 거리 조회 성공", shortestPath));
    }
}
