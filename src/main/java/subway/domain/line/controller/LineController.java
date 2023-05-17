package subway.domain.line.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import subway.domain.line.domain.Line;
import subway.domain.line.dto.LineDto;
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
        List<LineDto> lineDtos = lines.stream()
                .map(LineDto::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new ResultResponse(200, "전체 노선도 조회 성공", lineDtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse> findLineById(@PathVariable final Long id) {
        Line line = lineService.findById(id);
        return ResponseEntity.ok().body(new ResultResponse(200, "단일 노선도 조회 성공", LineDto.of(line)));
    }
}
