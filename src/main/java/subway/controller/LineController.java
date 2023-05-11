package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.service.LineService;
import subway.service.SubwayMapService;
import subway.entity.LineEntity;
import subway.dto.line.LineRequest;
import subway.dto.station.LineMapResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    // TODO: 테스트 추가, 도메인 메서드 및 변수명 변경, README 수정, 메서드 분리, 예외 핸들링, @Valid 적용

    /**
     * 1. 리팩토링 (코드 분리 + 도메인 메서드 명 변경)
     * 2. 테스트
     * 3. 예외 핸들링
     * 4. @Valid 적용
     * 5. README 수정
     */

    private final LineService lineService;
    private final SubwayMapService subwayMapService;

    public LineController(final LineService lineService, final SubwayMapService subwayMapService) {
        this.lineService = lineService;
        this.subwayMapService = subwayMapService;
    }

    @PostMapping
    public ResponseEntity<Long> createLine(@RequestBody LineRequest lineRequest) {
        Long id = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + id)).body(id);
    }

    @GetMapping
    public ResponseEntity<List<LineEntity>> findAllLines() {
        return ResponseEntity.ok(lineService.findAll());
    }

    @GetMapping("/{lineNumber}")
    public ResponseEntity<LineMapResponse> findLineById(@PathVariable final Long lineNumber) {
        return ResponseEntity.ok().body(subwayMapService.showLineMap(lineNumber));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
